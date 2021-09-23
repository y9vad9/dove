package com.dove.backend.apis.files

import com.dove.backend.storage.core.files.FilesInfoStorage
import com.dove.backend.storage.core.files.FilesStorage
import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.mocked.files.MockedFilesInfoStorage
import com.dove.backend.storage.mocked.files.MockedFilesStorage
import com.dove.backend.storage.mocked.users.MockedUsersStorage
import com.dove.data.monad.isSuccess
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import java.io.InputStream
import java.util.*

@Testable
object FilesAPITest {
    private val usersStorage: UsersStorage = MockedUsersStorage()
    private val filesInfoStorage: FilesInfoStorage = MockedFilesInfoStorage()
    private val filesStorage: FilesStorage = MockedFilesStorage()

    private val api: FilesAPI = FilesAPI(filesInfoStorage, filesStorage)

    private val user by lazy { runBlocking { usersStorage.create("test@testing.email") } }

    @Test
    fun upload(): Unit = runBlocking {
        assert(api.upload(user, "test", InputStream.nullInputStream()).isSuccess())
    }

    @Test
    fun getFileBytes(): Unit = runBlocking {
        val uuid = UUID.randomUUID().toString()
        val id = filesInfoStorage.create("test", uuid, uuid, user.id, 0)
        filesStorage.write(uuid, InputStream.nullInputStream())
        assert(api.getFileBytes(id).isSuccess())
    }
}