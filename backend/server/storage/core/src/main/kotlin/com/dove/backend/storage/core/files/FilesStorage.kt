package com.dove.backend.storage.core.files

import java.io.InputStream

interface FilesStorage {
    /**
     * Writes [inputStream] to storage.
     * @param fileName - file name.
     * @param inputStream - input stream to write.
     */
    suspend fun write(fileName: String, inputStream: InputStream)

    /**
     * Reads [fileName] in storage.
     * @return [InputStream] with file bytes.
     */
    suspend fun read(fileName: String): InputStream?
}