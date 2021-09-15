package com.dove.server.features.files.storage

import com.dove.data.files.FileInfo
import com.dove.server.utils.time.timeInMs
import java.util.*

class MockedFilesInfoStorage : FilesInfoStorage {
    private val files: MutableList<FileInfo> = mutableListOf()

    override suspend fun create(originalFileName: String, fileName: String, fileHash: String, fileOwner: Long): String {
        val uuid = UUID.randomUUID().toString()
        files += FileInfo(uuid, originalFileName, fileHash, fileOwner, timeInMs)
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