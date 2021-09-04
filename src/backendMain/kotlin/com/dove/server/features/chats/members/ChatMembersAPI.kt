package com.dove.server.features.chats.members

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.NoSuchPermissionError
import com.dove.data.monad.Either
import com.dove.data.monad.onError
import com.dove.data.monad.valueOrThrow
import com.dove.data.users.User
import com.dove.data.users.tokens.Token
import com.dove.server.features.chats.ChatHelper
import com.dove.server.features.users.UsersStorage
import com.dove.server.features.users.tokens.TokensHelper

object ChatMembersAPI {

    suspend fun getMembers(token: String, chatId: Long, numberToLoad: Int, offset: Long): ApiResult<List<User>> {
        val auth: Token = TokensHelper.checkRegularAuthorization(token).onError {
            return Either.error(it)
        }.valueOrThrow()

        ChatHelper.checkIsChatMember(chatId, auth.userId).onError {
            return Either.error(NoSuchPermissionError("you should be chat member to get members."))
        }

        return Either.success(ChatMembersStorage.readAll(chatId, numberToLoad, offset).map {
            UsersStorage.read(it.memberId)!!
        })
    }

    suspend fun addMember(token: String, chatId: Long, userId: Long): ApiResult<Unit> {
        val auth: Token = TokensHelper.checkRegularAuthorization(token).onError {
            return Either.error(it)
        }.valueOrThrow()

        ChatHelper.checkIsChatOwner(chatId, auth.userId).onError {
            return Either.error(it)
        }

        return Either.success(ChatMembersStorage.create(chatId, userId))
    }

    suspend fun kickMember(token: String, chatId: Long, userId: Long): ApiResult<Unit> {
        val auth: Token = TokensHelper.checkRegularAuthorization(token).onError {
            return Either.error(it)
        }.valueOrThrow()

        ChatHelper.checkIsChatOwner(chatId, auth.userId).onError {
            return Either.error(it)
        }

        return Either.success(ChatMembersStorage.delete(chatId, userId))
    }

}