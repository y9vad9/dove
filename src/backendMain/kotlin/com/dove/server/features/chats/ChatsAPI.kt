package com.dove.server.features.chats

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.ChatNotFoundError
import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import com.dove.data.chats.GroupInfo
import com.dove.data.chats.MemberType
import com.dove.data.monad.Either
import com.dove.data.monad.isSuccess
import com.dove.data.monad.onError
import com.dove.data.users.User
import com.dove.server.features.chats.members.ChatMembersStorage

object ChatsAPI {
    /**
     * Gets [number] user chats with [offset].
     */
    suspend fun getUserChats(user: User, number: Int, offset: Long): ApiResult<List<Chat>> {
        return Either.success(ChatsStorage.readAll(user.id, number, offset))
    }

    suspend fun createGroup(user: User, name: String): ApiResult<Chat.Group> {
        val chatId = ChatsStorage.create(name, null, ChatType.GROUP)
        ChatMembersStorage.create(chatId, user.id, MemberType.OWNER)
        return Either.success(ChatsStorage.read(chatId, user.id) as Chat.Group)
    }

    suspend fun createPersonalChat(user: User, anotherUserId: Long): ApiResult<Chat.Personal> {
        val chatId = ChatsStorage.create(null, null, ChatType.PERSONAL)
        ChatMembersStorage.create(chatId, user.id, MemberType.OWNER)
        ChatMembersStorage.create(chatId, anotherUserId, MemberType.OWNER)
        return Either.success(ChatsStorage.read(chatId, user.id) as Chat.Personal)
    }

    suspend fun updateGroupInfo(user: User, chatId: Long, groupInfo: GroupInfo): ApiResult<Unit> {
        val group = ChatsStorage.read(chatId, user.id)
            ?.takeIf { it.type == ChatType.GROUP }
            ?: return Either.error(ChatNotFoundError)

        ChatHelper.checkIsChatOwner(chatId, user.id).onError {
            return Either.error(it)
        }

        return Either.success(ChatsStorage.update(chatId, groupInfo.name, groupInfo.image))
    }

    suspend fun deleteChat(user: User, chatId: Long): ApiResult<Unit> {
        val check = ChatHelper.checkIsChatOwner(chatId, user.id)

        return if (check.isSuccess())
            Either.success(ChatsStorage.delete(chatId))
        else
            Either.error(check.error)
    }

}