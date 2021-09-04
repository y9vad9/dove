package com.dove.server.features.chats.messages

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.MessageNotFoundError
import com.dove.data.api.errors.NoSuchPermissionError
import com.dove.data.chats.messages.Message
import com.dove.data.chats.messages.MessageContent
import com.dove.data.monad.Either
import com.dove.data.monad.onError
import com.dove.data.monad.valueOrThrow
import com.dove.server.features.chats.ChatHelper
import com.dove.server.features.users.tokens.TokensHelper

object MessagesAPI {
    suspend fun getMessages(
        token: String,
        chatId: Long,
        numberOfMessages: Int,
        offset: Long
    ): ApiResult<List<Message>> {
        val auth = TokensHelper.checkRegularAuthorization(token).onError {
            return Either.error(it)
        }.valueOrThrow()

        ChatHelper.checkIsChatMember(chatId, auth.userId).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get messages."))
        }

        return Either.success(MessagesStorage.readAll(chatId, numberOfMessages, offset))
    }

    suspend fun sendMessage(token: String, chatId: Long, message: MessageContent<*>): ApiResult<Unit> {
        val auth = TokensHelper.checkRegularAuthorization(token).onError {
            return Either.error(it)
        }.valueOrThrow()
        ChatHelper.checkIsChatMember(chatId, auth.userId).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get messages."))
        }
        return Either.success(MessagesStorage.create(auth.userId, chatId, message.value.toString(), message.type))
    }

    suspend fun deleteMessage(token: String, chatId: Long, messageId: Long): ApiResult<Unit> {
        val auth = TokensHelper.checkRegularAuthorization(token).onError {
            return Either.error(it)
        }.valueOrThrow()

        ChatHelper.checkIsChatMember(chatId, auth.userId).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get messages."))
        }

        val message = MessagesStorage.read(messageId) ?: return Either.error(MessageNotFoundError)

        return if (message.user.id == auth.userId)
            Either.success(MessagesStorage.delete(messageId))
        else Either.error(NoSuchPermissionError("You can delete only your messages."))
    }

}