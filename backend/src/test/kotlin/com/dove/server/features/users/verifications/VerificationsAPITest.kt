package com.dove.server.features.users.verifications

import com.dove.data.monad.isSuccess
import com.dove.data.users.verifications.VerificationType
import com.dove.server.features.users.storage.MockedUsersStorage
import com.dove.server.features.users.storage.UsersStorage
import com.dove.server.features.users.tokens.storage.MockedTokensStorage
import com.dove.server.features.users.tokens.storage.TokensStorage
import com.dove.server.features.users.verifications.storage.MockedVerificationsStorage
import com.dove.server.features.users.verifications.storage.VerificationsStorage
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
        verificationsStorage.create(user.email, CODE, VerificationType.AUTH)
        assert(api.confirmAuth(user.email, CODE).isSuccess())
    }
}