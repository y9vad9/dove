package com.dove.server.features.users

import com.dove.data.Constants
import com.dove.data.monad.isSuccess
import com.dove.data.monad.onError
import com.dove.data.monad.valueOrThrow
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.FeaturesTest
import com.dove.server.features.users.tokens.TokensStorage
import com.dove.server.utils.random.nextString
import com.dove.server.utils.time.timeInMs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import kotlin.random.Random

@Testable
object UsersAPITest : FeaturesTest() {

    @BeforeAll
    override fun initialize(): Unit = runBlocking {
        super.initialize()
        System.setProperty("isTest", true.toString())
    }

    @BeforeEach
    fun removeItems() = runBlocking {
        UsersStorage.deleteAll()
    }

    @Test
    fun getByEmail(): Unit = runBlocking {
        val email = Random.nextString(10).plus("@email.net")
        val user = UsersStorage.create(email)
        val usersResult = UsersAPI.getByEmail(email).onError {
            throw AssertionError(it.message)
        }
        assert(usersResult.valueOrThrow().email == email)
        UsersStorage.deleteAll()
    }

    @Test
    fun getById(): Unit = runBlocking {
        val email = Random.nextString(10).plus("@email.net")
        val user = UsersStorage.create(email)
        assert(UsersAPI.getById(user.id).isSuccess())
        UsersStorage.deleteAll()
    }

    @Test
    fun getUsers(): Unit = runBlocking {
        val email1 = Random.nextString(10).plus("@email.net")
        val email2 = Random.nextString(10).plus("@email.net")
        UsersStorage.create(email1)
        UsersStorage.create(email2)
        assert(UsersAPI.getUsers().let { it.isSuccess() && it.valueOrThrow().isNotEmpty() })
    }

    @Test
    fun editProfile(): Unit = runBlocking {
        val email = Random.nextString(10).plus("@email.net")
        val token = Random.nextString(Constants.TOKEN_LENGTH)
        val user = UsersStorage.create(email)
        TokensStorage.create(user.id, token, timeInMs, TokenType.REGISTRATION)
        assert(UsersAPI.editProfile(token, "Test", "Nothing").isSuccess())
        assert(UsersStorage.read(email)!!.firstName == "Test")
    }
}