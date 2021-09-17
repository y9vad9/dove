package com.dove.server.features.files.storage

import java.io.InputStream

interface FilesStorage {

    companion object Default : FilesStorage by DirectoryFilesStorage(com.dove.server.Environment.files)

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