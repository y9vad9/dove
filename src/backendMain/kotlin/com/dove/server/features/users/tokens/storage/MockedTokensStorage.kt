package com.dove.server.features.users.tokens.storage

import com.dove.data.users.tokens.Token
import com.dove.data.users.tokens.TokenType
import com.dove.server.utils.time.timeInMs
import kotlin.random.Random

class MockedTokensStorage : TokensStorage {
    private val tokens: MutableList<Token> = mutableListOf()


    override suspend fun create(userId: Long, token: String, time: Long, type: TokenType): Token {
        val auth = Token(Random.nextLong(99999), userId, token, timeInMs, type)
        tokens += auth
        return auth
    }

    override suspend fun readAll(userId: Long): List<Token> {
        return tokens.filter { it.userId == userId }
    }

    override suspend fun read(token: String): Token? {
        return tokens.firstOrNull { it.token == token }
    }

    override suspend fun read(id: Long): Token? {
        return tokens.firstOrNull { it.tokenId == id }
    }

    override suspend fun delete(id: Long) {
        tokens.removeIf { it.tokenId == id }
    }

    override suspend fun delete(token: String) {
        tokens.removeIf { it.token == token }
    }

    override suspend fun deleteAll() {
        tokens.clear()
    }
}