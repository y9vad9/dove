package com.dove.server.features.users.tokens.storage

import com.dove.data.users.tokens.Token
import com.dove.data.users.tokens.TokenType
import org.jetbrains.annotations.TestOnly

interface TokensStorage {

    companion object Default : TokensStorage by DatabaseTokensStorage

    /**
     * Creates new token in storage.
     * @param userId - user identifier.
     * @param token - token string.
     * @param time - unix time in ms.
     */
    suspend fun create(userId: Long, token: String, time: Long, type: TokenType): Token

    /**
     * Gets all tokens of user by [userId].
     */
    suspend fun readAll(userId: Long): List<Token>

    /**
     * Gets [Token] by [token] string.
     */
    suspend fun read(token: String): Token?

    suspend fun read(id: Long): Token?

    /**
     * Deletes token with id [id].
     */
    suspend fun delete(id: Long)

    /**
     * Deletes token with [token].
     */
    suspend fun delete(token: String)

    @TestOnly
    suspend fun deleteAll()
}