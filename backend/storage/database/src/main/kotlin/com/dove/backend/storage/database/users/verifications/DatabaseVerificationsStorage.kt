package com.dove.backend.storage.database.users.verifications

import com.dove.backend.storage.core.users.verifications.VerificationsStorage
import com.dove.data.Constants
import com.dove.data.users.verifications.Verification
import com.dove.data.users.verifications.VerificationType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseVerificationsStorage(database: Database) : VerificationsStorage {
    private object Verifications : Table() {
        val EMAIL: Column<String> = varchar("email", Constants.EMAIL_MAX_LEN)
        val CODE: Column<String> = text("code")
        val TYPE: Column<VerificationType> = enumeration("type", VerificationType::class)
        val TIME: Column<Long> = long("time")
    }

    init {
        transaction(database) {
            SchemaUtils.create(Verifications)
        }
    }


    override suspend fun create(email: String, code: String, type: VerificationType, time: Long): Unit =
        newSuspendedTransaction {
            Verifications.insert {
                it[EMAIL] = email
                it[CODE] = code
                it[TYPE] = type
                it[TIME] = time
            }
        }

    override suspend fun deleteAll(): Unit = newSuspendedTransaction {
        Verifications.deleteAll()
    }

    override suspend fun read(email: String, code: String, type: VerificationType?): Verification? =
        newSuspendedTransaction {
            Verifications.select {
                (Verifications.EMAIL eq email) and (Verifications.CODE eq code) and
                    if (type != null) (Verifications.TYPE eq type) else Op.TRUE
            }.firstOrNull()?.toVerification()
        }

    override suspend fun delete(email: String, code: String, type: VerificationType): Unit = newSuspendedTransaction {
        Verifications.deleteWhere {
            (Verifications.EMAIL eq email) and (Verifications.CODE eq code) and (Verifications.TYPE eq type)
        }
    }

    private fun ResultRow.toVerification() = Verification(
        get(Verifications.EMAIL),
        get(Verifications.CODE),
        get(Verifications.TYPE),
        get(Verifications.TIME)
    )

}