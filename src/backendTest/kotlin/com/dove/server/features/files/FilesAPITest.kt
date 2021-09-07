package com.dove.server.features.files

import com.dove.data.Constants
import com.dove.data.monad.isSuccess
import com.dove.data.users.User
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.users.tokens.TokensStorage
import com.dove.server.utils.hashing.toMD5
import com.dove.server.utils.random.nextString
import com.dove.server.utils.time.timeInMs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.random.Random

@Testable
object FilesAPITest {
    private const val userId: Long = 1
    private const val realFileName = "read-file-name.ext"
    private val fileBytes: ByteArray = "test".toByteArray()
    private val user = User(0, "", "", "")

    private val inputStream
        get() = FileInputStream(File.createTempFile("test", "png").apply {
            createNewFile()
            writeBytes(fileBytes)
        })


    @BeforeEach
    fun beforeEach() = runBlocking {
        FilesStorage.deleteAll()
        TokensStorage.deleteAll()
    }

    @Test
    fun `upload test by invalid token`() = runBlocking {
        val token = Random.nextString(Constants.TOKEN_LENGTH)
        TokensStorage.create(userId, token, timeInMs, TokenType.REGISTRATION)
        assert(
            !FilesAPI.upload(
                user,
                realFileName,
                inputStream
            )
                .isSuccess()
        )
    }

    @Test
    fun `upload test by valid token`() = runBlocking {
        val token = Random.nextString(Constants.TOKEN_LENGTH)
        TokensStorage.create(userId, token, timeInMs, TokenType.REGULAR)
        assert(FilesAPI.upload(user, realFileName, inputStream).isSuccess())
    }

    @Test
    fun `get file bytes by invalid uuid`() = runBlocking {
        FilesStorage.create(realFileName, fileBytes.toMD5(), fileBytes.toMD5(), userId)
        assert(!FilesAPI.getFileBytes(UUID.randomUUID().toString()).isSuccess())
    }

    @Test
    fun `get file bytes by valid uuid`() = runBlocking {
        val uuid = FilesStorage.create(realFileName, fileBytes.toMD5(), fileBytes.toMD5(), userId)
        assert(FilesAPI.getFileBytes(uuid).isSuccess())
    }

}