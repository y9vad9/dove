package com.dove.backend.apis.chats

import com.dove.backend.features.time.timeInMs
import com.dove.backend.storage.core.chats.ChatsStorage
import com.dove.backend.storage.core.chats.members.ChatMembersStorage
import com.dove.data.api.ApiError.Companion.ChatNotFoundError
import com.dove.data.api.ApiResult
import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import com.dove.data.chats.MemberType
import com.dove.data.monad.Either
import com.dove.data.monad.isSuccess
import com.dove.data.monad.onError
import com.dove.data.users.User

class ChatsAPI(private val chatsStorage: ChatsStorage, private val chatMembersStorage: ChatMembersStorage) {
    /**
     * Gets [number] user chats with [offset].
     */
    suspend fun getUserChats(user: User, number: Int, offset: Long): ApiResult<List<Chat>> {
        return Either.success(chatsStorage.readAll(user.id, number, offset))
    }

    suspend fun createGroup(user: User, name: String): ApiResult<Chat.Group> {
        val chatId = chatsStorage.create(name, null, ChatType.GROUP, timeInMs)
        chatMembersStorage.create(chatId, user.id, MemberType.OWNER, timeInMs)
        return Either.success(chatsStorage.read(chatId, user.id) as Chat.Group)
    }

    suspend fun createPersonalChat(user: User, anotherUserId: Long): ApiResult<Chat.Personal> {
        val chatId = chatsStorage.create(null, null, ChatType.PERSONAL, timeInMs)
        chatMembersStorage.create(chatId, user.id, MemberType.OWNER, timeInMs)
        chatMembersStorage.create(chatId, anotherUserId, MemberType.OWNER, timeInMs)
        return Either.success(chatsStorage.read(chatId, user.id) as Chat.Personal)
    }

    suspend fun updateGroupInfo(user: User, chatId: Long, name: String?, image: String?): ApiResult<Unit> {
        chatsStorage.read(chatId, user.id)
            ?.takeIf { it is Chat.Group }
            ?: return Either.error(ChatNotFoundError)

        ChatHelper.checkIsChatOwner(chatMembersStorage, chatId, user.id).onError {
            return Either.error(it)
        }

        return Either.success(chatsStorage.update(chatId, name, image))
    }

    suspend fun deleteChat(user: User, chatId: Long): ApiResult<Unit> {
        val check = ChatHelper.checkIsChatOwner(chatMembersStorage, chatId, user.id)

        return if (check.isSuccess())
            Either.success(chatsStorage.delete(chatId))
        else
            Either.error(check.error)
    }

}