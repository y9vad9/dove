package com.dove.server.features.files

import com.dove.data.monad.isSuccess
import com.dove.server.features.files.storage.FilesInfoStorage
import com.dove.server.features.files.storage.FilesStorage
import com.dove.server.features.files.storage.MockedFilesInfoStorage
import com.dove.server.features.files.storage.MockedFilesStorage
import com.dove.server.features.users.storage.MockedUsersStorage
import com.dove.server.features.users.storage.UsersStorage
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
        val id = filesInfoStorage.create("test", uuid, uuid, user.id)
        filesStorage.write(uuid, InputStream.nullInputStream())
        assert(api.getFileBytes(id).isSuccess())
    }
}