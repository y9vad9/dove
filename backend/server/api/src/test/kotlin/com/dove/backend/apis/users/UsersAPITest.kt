package com.dove.backend.apis.users

import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.mocked.users.MockedUsersStorage
import com.dove.data.monad.isSuccess
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable

@Testable
object UsersAPITest {
    private val usersStorage: UsersStorage = MockedUsersStorage()
    private val api: UsersAPI = UsersAPI(usersStorage)

    private val user by lazy { runBlocking { usersStorage.create("test@testing.email") } }

    @Test
    fun getUserByEmail() = runBlocking {
        assert(api.getByEmail(user.email).isSuccess())
    }

    @Test
    fun getUserById() = runBlocking {
        assert(api.getById(user.id).isSuccess())
    }

    @Test
    fun getUsers() = runBlocking {
        assert(api.getUsers().isSuccess())
    }

    @Test
    fun editProfile() = runBlocking {
        val userName = "Test"
        assert(api.editProfile(user, userName).isSuccess())
        assert(usersStorage.read(user.id)!!.firstName == userName)
    }
}