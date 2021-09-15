package com.dove.server.features.files

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.FileNotFoundError
import com.dove.data.monad.Either
import com.dove.data.users.User
import com.dove.server.features.files.storage.FilesInfoStorage
import com.dove.server.features.files.storage.FilesStorage
import com.dove.server.utils.hashing.toMD5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*

class FilesAPI(private val filesInfoStorage: FilesInfoStorage, private val filesStorage: FilesStorage) {
    /**
     * Uploads file to server.
     * @return [String] representation of [java.util.UUID].
     */
    suspend fun upload(user: User, realName: String, stream: InputStream): ApiResult<String> {
        val fileHash = stream.toMD5()
        filesStorage.write(fileHash, stream)
        return Either.success(filesInfoStorage.create(realName, fileHash, fileHash, user.id))
    }

    /**
     * Reads file bytes by [fileUUID].
     */
    suspend fun getFileBytes(fileUUID: String): ApiResult<InputStream> {
        val file = filesInfoStorage.read(UUID.fromString(fileUUID)) ?: return Either.error(FileNotFoundError)
        return Either.success(withContext(Dispatchers.IO) {
            filesStorage.read(file.fileHash)
        })
    }
}