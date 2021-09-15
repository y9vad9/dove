package com.dove.server.features.chats.members

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.NoSuchPermissionError
import com.dove.data.chats.MemberType
import com.dove.data.monad.Either
import com.dove.data.monad.onError
import com.dove.data.users.User
import com.dove.server.features.chats.ChatHelper
import com.dove.server.features.chats.members.storage.ChatMembersStorage
import com.dove.server.features.users.storage.DatabaseUsersStorage

class ChatMembersAPI(private val storage: ChatMembersStorage) {

    suspend fun getMembers(user: User, chatId: Long, numberToLoad: Int, offset: Long): ApiResult<List<User>> {
        ChatHelper.checkIsChatMember(storage, chatId, user.id).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get members."))
        }

        return Either.success(storage.readAll(chatId, numberToLoad, offset).map {
            DatabaseUsersStorage.read(it.memberId)!!
        })
    }

    suspend fun addMember(user: User, chatId: Long, userId: Long): ApiResult<Unit> {
        ChatHelper.checkIsChatOwner(storage, chatId, user.id).onError {
            return Either.error(it)
        }

        return Either.success(storage.create(chatId, userId, MemberType.REGULAR))
    }

    suspend fun kickMember(user: User, chatId: Long, userId: Long): ApiResult<Unit> {
        ChatHelper.checkIsChatOwner(storage, chatId, user.id).onError {
            return Either.error(it)
        }

        return Either.success(storage.delete(chatId, userId))
    }

}