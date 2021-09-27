package com.dove.backend.storage.database.users

import com.dove.backend.storage.core.users.UsersStorage
import com.dove.data.Constants
import com.dove.data.users.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseUsersStorage(database: Database) : UsersStorage {
    private object Users : Table() {
        val ID: Column<Long> = long("id").autoIncrement()
        val EMAIL: Column<String> = varchar("email", Constants.EMAIL_MAX_LEN)
        val FIRST_NAME: Column<String> = varchar("first_name", Constants.FIRST_NAME_MAX_LEN).default("User")
        val LAST_NAME: Column<String?> = varchar("last_name", Constants.LAST_NAME_MAX_LEN).nullable()
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    /**
     * Gets user by [id].
     */
    override suspend fun read(id: Long): User? = newSuspendedTransaction {
        Users.select { Users.ID eq id }.firstOrNull()?.toUser()
    }

    /**
     * Gets users by [ids] collection.
     * @param ids - ids of users to get.
     * @return [List] of [User]s.
     */
    override suspend fun readAll(ids: Collection<Long>): List<User> {
        val cached = mutableSetOf<User>()
        return ids.map { id ->
            cached.firstOrNull { it.id == id } ?: newSuspendedTransaction {
                Users.select { Users.ID eq id }.first().toUser().also { user ->
                    cached += user
                }
            }
        }
    }

    /**
     * Gets user by [email].
     */
    override suspend fun read(email: String) = newSuspendedTransaction {
        Users.select { Users.EMAIL eq email }.firstOrNull()?.toUser()
    }

    /**
     * Gets all users that satisfy [query].
     * @param query - search query.
     * @param number - number of items that will be returned.
     * @param offset - offset in database.
     */
    override suspend fun readAll(query: String?, number: Int, offset: Long): List<User> = newSuspendedTransaction {
        Users.select {
            if (query != null)
            // TODO somehow concat
                (Users.FIRST_NAME like query) or (Users.LAST_NAME like query)
            else Op.TRUE
        }.limit(number, offset)
            .toList()
            .map {
                it.toUser()
            }
    }

    /**
     * Creates user with [email].
     * @return [User]
     */
    override suspend fun create(email: String): User = newSuspendedTransaction {
        Users.insert {
            it[EMAIL] = email
        }.resultedValues!!.first().toUser()
    }

    /**
     * Deletes user with id that equals to [id].
     */
    override suspend fun delete(id: Long): Unit = newSuspendedTransaction {
        Users.deleteWhere { Users.ID eq id }
    }

    override suspend fun deleteAll(): Unit = newSuspendedTransaction {
        Users.deleteAll()
    }

    /**
     * Updates user with id [id].
     */
    override suspend fun update(id: Long, newFirstName: String?, newLastName: String?): Unit = newSuspendedTransaction {
        Users.update(
            where = { Users.ID eq id },
            body = {
                if (newFirstName != null)
                    it[FIRST_NAME] = newFirstName
                if (newLastName != null)
                    it[LAST_NAME] = newLastName
            }
        )
    }

    private fun ResultRow.toUser() = User(get(Users.ID), get(Users.FIRST_NAME), get(Users.LAST_NAME), get(Users.EMAIL))
}