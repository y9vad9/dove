package com.dove.server.features.files.storage

import com.dove.data.Constants
import com.dove.data.files.FileInfo
import com.dove.server.di.DatabaseDI
import com.dove.server.utils.time.timeInMs
import org.jetbrains.annotations.TestOnly
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object DatabaseFilesInfoStorage : FilesInfoStorage {
    private object Files : Table() {
        val FILE_UUID = uuid("file_uuid").clientDefault { UUID.randomUUID() }
        val ORIGINAL_FILE_NAME = varchar("original_file_name", Constants.FILE_NAME_MAX_LEN)
        val FILE_NAME = text("file_name")
        val FILE_HASH = varchar("file_hash", 512)
        val FILE_UPLOAD_TIME = long("file_upload_time").clientDefault { timeInMs }
        val FILE_OWNER = long("file_owner")
    }

    init {
        transaction(DatabaseDI.database) {
            SchemaUtils.create(Files)
        }
    }

    /**
     * Creates new file in files table.
     * @param originalFileName - client-original name of file.
     * @param fileName - name of file that uploaded on com.dove.server (different from [originalFileName]).
     * @param fileHash - MD5 hash of file.
     * @param fileOwner - file owner id.
     */
    override suspend fun create(
        originalFileName: String,
        fileName: String,
        fileHash: String,
        fileOwner: Long
    ): String = newSuspendedTransaction {
        Files.insert {
            it[FILE_NAME] = fileName
            it[ORIGINAL_FILE_NAME] = originalFileName
            it[FILE_HASH] = fileHash
            it[FILE_OWNER] = fileOwner
        }.get(Files.FILE_UUID).toString()
    }

    /**
     * Gets [FileInfo] by [fileName].
     * @param uuid - universally unique identifier of file.
     * @return [FileInfo] or null if file does not exist.
     */
    override suspend fun read(uuid: UUID): FileInfo? = newSuspendedTransaction {
        Files.select { Files.FILE_UUID eq uuid }.firstOrNull()?.toFileInfo()
    }

    /**
     * Deletes file with [uuid].
     */
    override suspend fun delete(uuid: UUID): Unit = newSuspendedTransaction {
        Files.deleteWhere { Files.FILE_UUID eq uuid }
    }

    @TestOnly
    override suspend fun deleteAll(): Unit = newSuspendedTransaction {
        Files.deleteAll()
    }

    override suspend fun readAll(fileIds: Collection<String>): List<FileInfo> {
        val cached = mutableSetOf<FileInfo>()
        return fileIds.map { uuid ->
            cached.firstOrNull { it.uuid == uuid } ?: newSuspendedTransaction {
                Files.select { Files.FILE_UUID eq UUID.fromString(uuid) }.first().toFileInfo()
            }
        }
    }

    private fun ResultRow.toFileInfo() = FileInfo(
        uuid = get(Files.FILE_UUID).toString(),
        originalFileName = get(Files.ORIGINAL_FILE_NAME),
        fileHash = get(Files.FILE_HASH),
        fileOwnerId = get(Files.FILE_OWNER),
        uploadTime = get(Files.FILE_UPLOAD_TIME)
    )

}