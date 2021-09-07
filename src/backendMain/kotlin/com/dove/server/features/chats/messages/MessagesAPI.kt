package com.dove.server.features.chats.messages

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.MessageNotFoundError
import com.dove.data.api.errors.NoSuchPermissionError
import com.dove.data.chats.messages.Message
import com.dove.data.chats.messages.MessageContent
import com.dove.data.monad.Either
import com.dove.data.monad.onError
import com.dove.data.users.User
import com.dove.server.features.chats.ChatHelper

object MessagesAPI {
    suspend fun getMessages(
        user: User,
        chatId: Long,
        numberOfMessages: Int,
        offset: Long
    ): ApiResult<List<Message>> {
        ChatHelper.checkIsChatMember(chatId, user.id).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get messages."))
        }

        return Either.success(MessagesStorage.readAll(chatId, numberOfMessages, offset))
    }

    suspend fun sendMessage(user: User, chatId: Long, message: MessageContent<*>): ApiResult<Unit> {
        ChatHelper.checkIsChatMember(chatId, user.id).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get messages."))
        }
        return Either.success(MessagesStorage.create(user.id, chatId, message.value.toString(), message.type))
    }

    suspend fun deleteMessage(user: User, chatId: Long, messageId: Long): ApiResult<Unit> {
        ChatHelper.checkIsChatMember(chatId, user.id).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get messages."))
        }

        val message = MessagesStorage.read(messageId) ?: return Either.error(MessageNotFoundError)

        return if (message.user.id == user.id)
            Either.success(MessagesStorage.delete(messageId))
        else Either.error(NoSuchPermissionError("You can delete only your messages."))
    }

}