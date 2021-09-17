package com.dove.server.features.files.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

@Suppress("BlockingMethodInNonBlockingContext")
class DirectoryFilesStorage(private val directory: Path) : FilesStorage {
    override suspend fun write(fileName: String, inputStream: InputStream): Unit = withContext(Dispatchers.IO) {
        directory.resolve(fileName).apply {
            createFile()
            inputStream.transferTo(outputStream())
        }
    }

    override suspend fun read(fileName: String): InputStream = withContext(Dispatchers.IO) {
        return@withContext directory.resolve(fileName).inputStream()
    }
}