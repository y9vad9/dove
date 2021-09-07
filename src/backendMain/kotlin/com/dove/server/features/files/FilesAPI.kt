package com.dove.server.features.files

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.FileNotFoundError
import com.dove.data.monad.Either
import com.dove.data.users.User
import com.dove.data.users.tokens.TokenType
import com.dove.server.local.Environment
import com.dove.server.utils.hashing.toMD5
import com.papsign.ktor.openapigen.content.type.multipart.NamedFileInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*
import kotlin.io.path.createFile
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

object FilesAPI {
    /**
     * Uploads file to server.
     * @param token - authorization token (token should be [TokenType.REGULAR])
     * @return [String] representation of [java.util.UUID].
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun upload(user: User, realName: String, stream: NamedFileInputStream): ApiResult<String> {
        val fileHash = stream.toMD5()
        withContext(Dispatchers.IO) {
            Environment.files.resolve(fileHash).apply {
                createFile()
                stream.transferTo(outputStream())
            }
        }
        return Either.success(FilesStorage.create(realName, fileHash, fileHash, user.id))
    }

    /**
     * Reads file bytes by [fileUUID].
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getFileBytes(fileUUID: String): ApiResult<InputStream> {
        val file = FilesStorage.read(UUID.fromString(fileUUID)) ?: return Either.error(FileNotFoundError)
        return Either.success(withContext(Dispatchers.IO) {
            Environment.files.resolve(file.fileHash).inputStream()
        })
    }
}