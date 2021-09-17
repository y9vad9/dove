package com.dove.server.features.users

import com.dove.data.monad.isSuccess
import com.dove.server.features.users.storage.MockedUsersStorage
import com.dove.server.features.users.storage.UsersStorage
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