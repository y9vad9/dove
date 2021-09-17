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
import com.dove.server.features.chats.members.storage.ChatMembersStorage
import com.dove.server.features.chats.messages.storage.MessagesStorage

class MessagesAPI(private val messagesStorage: MessagesStorage, private val membersStorage: ChatMembersStorage) {
    suspend fun getMessages(
        user: User,
        chatId: Long,
        numberOfMessages: Int,
        offset: Long
    ): ApiResult<List<Message>> {
        ChatHelper.checkIsChatMember(membersStorage, chatId, user.id).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get messages."))
        }

        return Either.success(messagesStorage.readAll(chatId, numberOfMessages, offset))
    }

    suspend fun sendMessage(user: User, chatId: Long, message: MessageContent<*>): ApiResult<Unit> {
        ChatHelper.checkIsChatMember(membersStorage, chatId, user.id).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get messages."))
        }
        return Either.success(messagesStorage.create(user.id, chatId, message))
    }

    suspend fun deleteMessage(user: User, chatId: Long, messageId: Long): ApiResult<Unit> {
        ChatHelper.checkIsChatMember(membersStorage, chatId, user.id).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get messages."))
        }

        val message = messagesStorage.read(messageId) ?: return Either.error(MessageNotFoundError)

        return if (message.user.id == user.id)
            Either.success(messagesStorage.delete(messageId))
        else Either.error(NoSuchPermissionError("You can delete only your messages."))
    }

}