package com.dove.server.features.users.storage

import com.dove.data.users.User
import org.jetbrains.annotations.TestOnly

interface UsersStorage {

    /**
     * Default [UsersStorage] realization.
     */
    companion object Default : UsersStorage by DatabaseUsersStorage

    /**
     * Gets user by [id].
     * @return [User] or `null` if there is no user with such id.
     */
    suspend fun read(id: Long): User?

    /**
     * Gets user by [email].
     * @return [User] or `null` if there is no user with such email.
     */
    suspend fun read(email: String): User?

    /**
     * Gets users by [ids] collection.
     * @param ids - ids of users to get.
     * @return [List] of [User]s.
     */
    suspend fun readAll(ids: Collection<Long>): List<User>

    /**
     * Gets all users by [query].
     * @param query - search query.
     * @param number - number of items that will be returned.
     * @param offset - offset in database.
     * @return [List] of [User]s that satisfy [query].
     */
    suspend fun readAll(query: String?, number: Int, offset: Long): List<User>

    /**
     * Creates user with [email].
     * @return registered [User].
     */
    suspend fun create(email: String): User

    /**
     * Deletes user with [id].
     */
    suspend fun delete(id: Long)

    /**
     * Deletes everything in database.
     */
    @TestOnly
    suspend fun deleteAll()

    /**
     * Updates user's profile with id [id].
     */
    suspend fun update(id: Long, newFirstName: String?, newLastName: String?)
}