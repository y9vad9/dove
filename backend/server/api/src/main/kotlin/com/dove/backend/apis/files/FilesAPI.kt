package com.dove.backend.apis.files

import com.dove.backend.features.time.timeInMs
import com.dove.backend.storage.core.files.FilesInfoStorage
import com.dove.backend.storage.core.files.FilesStorage
import com.dove.data.api.ApiError
import com.dove.data.api.ApiResult
import com.dove.data.monad.Either
import com.dove.data.users.User
import com.y9neon.feature.hashing.toMD5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*

class FilesAPI(private val filesInfoStorage: FilesInfoStorage, private val filesStorage: FilesStorage) {
    /**
     * Uploads file to com.dove.server.
     * @return [String] representation of [java.util.UUID].
     */
    suspend fun upload(user: User, realName: String, stream: InputStream): ApiResult<String> {
        val fileHash = stream.toMD5()
        filesStorage.write(fileHash, stream)
        return Either.success(filesInfoStorage.create(realName, fileHash, fileHash, user.id, timeInMs))
    }

    /**
     * Reads file bytes by [fileUUID].
     */
    suspend fun getFileBytes(fileUUID: String): ApiResult<InputStream> {
        val file = filesInfoStorage.read(UUID.fromString(fileUUID)) ?: return Either.error(ApiError.FileNotFoundError)
        return Either.success(withContext(Dispatchers.IO) {
            filesStorage.read(file.fileHash)!!
        })
    }
}