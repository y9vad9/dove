package com.dove.backend.storage.mocked.files

import com.dove.backend.storage.core.files.FilesStorage
import java.io.InputStream

class MockedFilesStorage : FilesStorage {
    private val files: MutableList<String> = mutableListOf()

    override suspend fun write(fileName: String, inputStream: InputStream) {
        files += fileName
    }

    override suspend fun read(fileName: String): InputStream {
        return InputStream.nullInputStream()
    }
}