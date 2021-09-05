package com.dove.server.features.users.tokens

import com.dove.data.Constants
import com.dove.data.users.tokens.Token
import com.dove.data.users.tokens.TokenType
import com.dove.server.di.DatabaseDI
import com.dove.server.features.users.tokens.TokensStorage.Tokens.ID
import com.dove.server.features.users.tokens.TokensStorage.Tokens.TIME
import com.dove.server.features.users.tokens.TokensStorage.Tokens.TOKEN
import com.dove.server.features.users.tokens.TokensStorage.Tokens.TYPE
import com.dove.server.features.users.tokens.TokensStorage.Tokens.USER_ID
import org.jetbrains.annotations.TestOnly
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object TokensStorage {
    private object Tokens : Table() {
        val ID: Column<Long> = long("id").autoIncrement()
        val USER_ID: Column<Long> = long("user_id")
        val TOKEN: Column<String> = varchar("token", Constants.TOKEN_LENGTH)
        val TIME: Column<Long> = long("time")
        val TYPE: Column<TokenType> = enumeration("type", TokenType::class)
    }

    init {
        transaction(DatabaseDI.database) {
            SchemaUtils.create(Tokens)
        }
    }

    /**
     * Creates new token in storage.
     * @param userId - user identifier.
     * @param token - token string.
     * @param time - unix time in ms.
     */
    suspend fun create(userId: Long, token: String, time: Long, type: TokenType): Token = newSuspendedTransaction {
        Tokens.insert {
            it[USER_ID] = userId
            it[TOKEN] = token
            it[TIME] = time
            it[TYPE] = type
        }.resultedValues!!.first().toToken()
    }

    /**
     * Gets all tokens of user by [userId].
     */
    suspend fun readAll(userId: Long): List<Token> = newSuspendedTransaction {
        Tokens.select { USER_ID eq userId }.toList().map { it.toToken() }
    }

    /**
     * Gets [Token] by [token] string.
     */
    suspend fun read(token: String): Token? = newSuspendedTransaction {
        Tokens.select { TOKEN eq token }.firstOrNull()?.toToken()
    }

    suspend fun read(id: Long): Token? = newSuspendedTransaction {
        Tokens.select { ID eq id }.firstOrNull()?.toToken()
    }

    /**
     * Deletes token with [token].
     */
    suspend fun delete(token: String): Unit = newSuspendedTransaction {
        Tokens.deleteWhere { TOKEN eq token }
    }

    @TestOnly
    suspend fun deleteAll(): Unit = newSuspendedTransaction {
        Tokens.deleteAll()
    }

    /**
     * Deletes token with id [id].
     */
    suspend fun delete(id: Long): Unit = newSuspendedTransaction {
        Tokens.deleteWhere { ID eq id }
    }

    private fun ResultRow.toToken() = Token(get(ID), get(USER_ID), get(TOKEN), get(TIME), get(TYPE))
}