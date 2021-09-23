package com.dove.backend.storage.core.files

import com.dove.data.files.FileInfo
import org.jetbrains.annotations.TestOnly
import java.util.*

interface FilesInfoStorage {
    /**
     * Creates new file in files table.
     * @param originalFileName - client-original name of file.
     * @param fileName - name of file that uploaded on com.dove.server (different from [originalFileName]).
     * @param fileHash - MD5 hash of file.
     * @param fileOwner - file owner id.
     */
    suspend fun create(
        originalFileName: String,
        fileName: String,
        fileHash: String,
        fileOwner: Long,
        time: Long
    ): String

    /**
     * Gets [FileInfo] by [uuid].
     * @param uuid - universally unique identifier of file.
     * @return [FileInfo] or null if file does not exist.
     */
    suspend fun read(uuid: UUID): FileInfo?

    /**
     * Gets all files by [fileIds].
     * @return [List] of [FileInfo].
     */
    suspend fun readAll(fileIds: Collection<String>): List<FileInfo>

    /**
     * Deletes file with [uuid].
     */
    suspend fun delete(uuid: UUID)

    /**
     * Deletes everything in database.
     */
    @TestOnly
    suspend fun deleteAll()
}