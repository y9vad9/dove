package com.dove.server.features.users

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.UserNotFoundError
import com.dove.data.monad.Either
import com.dove.data.users.User
import com.dove.server.features.users.storage.UsersStorage

class UsersAPI(private val storage: UsersStorage) {

    /**
     * Gets user by [email].
     * Possible errors: [UserNotFoundError].
     */
    suspend fun getByEmail(email: String): ApiResult<User> {
        return storage.read(email)
            ?.let(Either.Companion::success)
            ?: Either.error(UserNotFoundError)
    }

    /**
     * Gets user by id.
     */
    suspend fun getById(id: Long): ApiResult<User> {
        return storage.read(id)
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
        return Either.success(storage.readAll(query, count, offset))
    }

    suspend fun editProfile(
        user: User,
        newFirstName: String? = null,
        newLastName: String? = null
    ): ApiResult<Unit> {
        return Either.success(storage.update(user.id, newFirstName, newLastName))
    }

}