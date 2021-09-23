package com.dove.backend.storage.mocked.files

import com.dove.backend.storage.core.files.FilesInfoStorage
import com.dove.data.files.FileInfo
import java.util.*

class MockedFilesInfoStorage : FilesInfoStorage {
    private val files: MutableList<FileInfo> = mutableListOf()

    override suspend fun create(
        originalFileName: String,
        fileName: String,
        fileHash: String,
        fileOwner: Long,
        time: Long
    ): String {
        val uuid = UUID.randomUUID().toString()
        files += FileInfo(uuid, originalFileName, fileHash, fileOwner, time)
        return uuid
    }

    override suspend fun read(uuid: UUID): FileInfo? {
        return files.firstOrNull { it.uuid == uuid.toString() }
    }

    override suspend fun readAll(fileIds: Collection<String>): List<FileInfo> {
        return fileIds.map {
            read(UUID.fromString(it))!!
        }
    }

    override suspend fun delete(uuid: UUID) {
        files.removeIf { it.uuid == uuid.toString() }
    }

    override suspend fun deleteAll() {
        files.clear()
    }
}