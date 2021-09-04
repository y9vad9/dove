package com.dove.server.features.files

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.FileNotFoundError
import com.dove.data.api.errors.InvalidTokenError
import com.dove.data.monad.Either
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.users.tokens.TokensStorage
import com.dove.server.local.Environment
import com.dove.server.utils.hashing.toMD5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.io.path.createFile
import kotlin.io.path.readBytes
import kotlin.io.path.writeBytes

object FilesAPI {
    /**
     * Uploads file to server.
     * @param token - authorization token (token should be [TokenType.REGULAR])
     * @return [String] representation of [java.util.UUID].
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun upload(token: String, realName: String, bytes: ByteArray): ApiResult<String> {
        val auth = TokensStorage.read(token) ?: return Either.error(InvalidTokenError())
        return if (auth.type != TokenType.REGULAR)
            Either.error(InvalidTokenError())
        else {
            val fileHash = bytes.toMD5()
            withContext(Dispatchers.IO) {
                Environment.files.resolve(fileHash).apply {
                    createFile()
                    writeBytes(bytes)
                }
            }
            Either.success(FilesStorage.create(realName, fileHash, fileHash, auth.userId))
        }
    }

    /**
     * Reads file bytes by [fileUUID].
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getFileBytes(fileUUID: String): ApiResult<ByteArray> {
        val file = FilesStorage.read(UUID.fromString(fileUUID)) ?: return Either.error(FileNotFoundError)
        return Either.success(withContext(Dispatchers.IO) {
            Environment.files.resolve(file.fileHash).readBytes()
        })
    }
}