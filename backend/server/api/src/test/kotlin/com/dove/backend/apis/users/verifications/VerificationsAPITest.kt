package com.dove.backend.apis.users.verifications

import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.core.users.tokens.TokensStorage
import com.dove.backend.storage.core.users.verifications.VerificationsStorage
import com.dove.backend.storage.mocked.users.MockedUsersStorage
import com.dove.backend.storage.mocked.users.tokens.MockedTokensStorage
import com.dove.backend.storage.mocked.users.verifications.MockedVerificationsStorage
import com.dove.data.monad.isSuccess
import com.dove.data.users.verifications.VerificationType
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable

@Testable
object VerificationsAPITest {
    private const val CODE = "0Q1W3E"

    private val verificationsStorage: VerificationsStorage = MockedVerificationsStorage()
    private val usersStorage: UsersStorage = MockedUsersStorage()
    private val tokensStorage: TokensStorage = MockedTokensStorage()

    private val api: VerificationsAPI = VerificationsAPI(verificationsStorage, usersStorage, tokensStorage)

    private val user by lazy { runBlocking { usersStorage.create("testing@email.com") } }

    @Test
    fun confirmAuth(): Unit = runBlocking {
        verificationsStorage.create(user.email, CODE, VerificationType.AUTH, 0)
        assert(api.confirmAuth(user.email, CODE).isSuccess())
    }
}