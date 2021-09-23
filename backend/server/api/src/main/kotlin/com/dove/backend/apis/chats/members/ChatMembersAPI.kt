package com.dove.backend.apis.chats.members

import com.dove.backend.apis.chats.ChatHelper
import com.dove.backend.features.time.timeInMs
import com.dove.backend.storage.core.chats.members.ChatMembersStorage
import com.dove.backend.storage.core.users.UsersStorage
import com.dove.data.api.ApiError.Companion.permissionError
import com.dove.data.api.ApiResult
import com.dove.data.chats.MemberType
import com.dove.data.monad.Either
import com.dove.data.monad.onError
import com.dove.data.users.User

class ChatMembersAPI(private val storage: ChatMembersStorage, private val usersStorage: UsersStorage) {

    suspend fun getMembers(user: User, chatId: Long, numberToLoad: Int, offset: Long): ApiResult<List<User>> {
        ChatHelper.checkIsChatMember(storage, chatId, user.id).onError {
            return Either.error(permissionError("you should be chat member to get members."))
        }

        return Either.success(storage.readAll(chatId, numberToLoad, offset).map {
            usersStorage.read(it.memberId)!!
        })
    }

    suspend fun addMember(user: User, chatId: Long, userId: Long): ApiResult<Unit> {
        ChatHelper.checkIsChatOwner(storage, chatId, user.id).onError {
            return Either.error(it)
        }

        return Either.success(storage.create(chatId, userId, MemberType.REGULAR, timeInMs))
    }

    suspend fun kickMember(user: User, chatId: Long, userId: Long): ApiResult<Unit> {
        ChatHelper.checkIsChatOwner(storage, chatId, user.id).onError {
            return Either.error(it)
        }

        return Either.success(storage.delete(chatId, userId))
    }

}