package com.dove.server.features.files.storage

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