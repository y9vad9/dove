package com.dove.backend.storage.database.users.tokens

import com.dove.backend.storage.core.users.tokens.TokensStorage
import com.dove.backend.storage.database.users.tokens.DatabaseTokensStorage.Tokens.ID
import com.dove.backend.storage.database.users.tokens.DatabaseTokensStorage.Tokens.TIME
import com.dove.backend.storage.database.users.tokens.DatabaseTokensStorage.Tokens.TOKEN
import com.dove.backend.storage.database.users.tokens.DatabaseTokensStorage.Tokens.TYPE
import com.dove.backend.storage.database.users.tokens.DatabaseTokensStorage.Tokens.USER_ID
import com.dove.data.Constants
import com.dove.data.users.tokens.Token
import com.dove.data.users.tokens.TokenType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseTokensStorage(database: Database) : TokensStorage {
    private object Tokens : Table() {
        val ID: Column<Long> = long("id").autoIncrement()
        val USER_ID: Column<Long> = long("user_id")
        val TOKEN: Column<String> = varchar("token", Constants.TOKEN_LENGTH)
        val TIME: Column<Long> = long("time")
        val TYPE: Column<TokenType> = enumeration("type", TokenType::class)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Tokens)
        }
    }

    /**
     * Creates new token in storage.
     * @param userId - user identifier.
     * @param token - token string.
     * @param time - unix time in ms.
     */
    override suspend fun create(userId: Long, token: String, time: Long, type: TokenType): Token =
        newSuspendedTransaction {
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
    override suspend fun readAll(userId: Long): List<Token> = newSuspendedTransaction {
        Tokens.select { USER_ID eq userId }.toList().map { it.toToken() }
    }

    /**
     * Gets [Token] by [token] string.
     */
    override suspend fun read(token: String): Token? = newSuspendedTransaction {
        Tokens.select { TOKEN eq token }.firstOrNull()?.toToken()
    }

    override suspend fun read(id: Long): Token? = newSuspendedTransaction {
        Tokens.select { ID eq id }.firstOrNull()?.toToken()
    }

    /**
     * Deletes token with [token].
     */
    override suspend fun delete(token: String): Unit = newSuspendedTransaction {
        Tokens.deleteWhere { TOKEN eq token }
    }

    override suspend fun deleteAll(): Unit = newSuspendedTransaction {
        Tokens.deleteAll()
    }

    /**
     * Deletes token with id [id].
     */
    override suspend fun delete(id: Long): Unit = newSuspendedTransaction {
        Tokens.deleteWhere { ID eq id }
    }

    private fun ResultRow.toToken() = Token(get(ID), get(USER_ID), get(TOKEN), get(TIME), get(TYPE))
}