package com.dove.server.features.users

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.InvalidTokenError
import com.dove.data.api.errors.UserNotFoundError
import com.dove.data.monad.Either
import com.dove.data.users.User
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.users.tokens.TokensStorage

object UsersAPI {

    /**
     * Gets user by [email].
     * Possible errors: [UserNotFoundError].
     */
    suspend fun getByEmail(email: String): ApiResult<User> {
        return UsersStorage.read(email)
            ?.let(Either.Companion::success)
            ?: Either.error(UserNotFoundError)
    }

    /**
     * Gets user by id.
     */
    suspend fun getById(id: Long): ApiResult<User> {
        return UsersStorage.read(id)
            ?.let(Either.Companion::success)
            ?: Either.error(UserNotFoundError)
    }

    /**
     * Searches users by [query]
     */
    suspend fun getUsers(
        query: String? = null,
        count: Int = 20,
        offset: Long = 0
    ): ApiResult<List<User>> {
        return Either.success(UsersStorage.readAll(query, count, offset))
    }

    suspend fun editProfile(
        token: String,
        newFirstName: String? = null,
        newLastName: String? = null
    ): ApiResult<Unit> {
        val auth = TokensStorage.read(token) ?: return Either.error(InvalidTokenError())

        if (!(auth.type == TokenType.REGISTRATION || auth.type == TokenType.REGULAR))
            return Either.error(InvalidTokenError())

        return Either.success(UsersStorage.update(auth.userId, newFirstName, newLastName))
    }

}