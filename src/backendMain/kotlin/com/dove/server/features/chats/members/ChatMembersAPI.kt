package com.dove.server.features.chats.members

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.NoSuchPermissionError
import com.dove.data.chats.MemberType
import com.dove.data.monad.Either
import com.dove.data.monad.onError
import com.dove.data.users.User
import com.dove.server.features.chats.ChatHelper
import com.dove.server.features.users.UsersStorage

object ChatMembersAPI {

    suspend fun getMembers(user: User, chatId: Long, numberToLoad: Int, offset: Long): ApiResult<List<User>> {
        ChatHelper.checkIsChatMember(chatId, user.id).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get members."))
        }

        return Either.success(ChatMembersStorage.readAll(chatId, numberToLoad, offset).map {
            UsersStorage.read(it.memberId)!!
        })
    }

    suspend fun addMember(user: User, chatId: Long, userId: Long): ApiResult<Unit> {
        ChatHelper.checkIsChatOwner(chatId, user.id).onError {
            return Either.error(it)
        }

        return Either.success(ChatMembersStorage.create(chatId, userId, MemberType.REGULAR))
    }

    suspend fun kickMember(user: User, chatId: Long, userId: Long): ApiResult<Unit> {
        ChatHelper.checkIsChatOwner(chatId, user.id).onError {
            return Either.error(it)
        }

        return Either.success(ChatMembersStorage.delete(chatId, userId))
    }

}