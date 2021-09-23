package com.dove.backend.apis.users.tokens

import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.core.users.tokens.TokensStorage
import com.dove.backend.storage.core.users.verifications.VerificationsStorage
import com.dove.backend.storage.mocked.users.MockedUsersStorage
import com.dove.backend.storage.mocked.users.tokens.MockedTokensStorage
import com.dove.backend.storage.mocked.users.verifications.MockedVerificationsStorage
import com.dove.data.Constants
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrThrow
import com.dove.data.users.tokens.TokenType
import com.dove.mailer.LocalMailer
import kotlinx.coroutines.runBlocking
import nextString
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import kotlin.random.Random

@Testable
object TokensAPITest {
    private const val EMAIL = "test@mail.net"

    private val tokensStorage: TokensStorage = MockedTokensStorage()
    private val usersStorage: UsersStorage = MockedUsersStorage()
    private val verificationsStorage: VerificationsStorage = MockedVerificationsStorage()

    private val mailer: LocalMailer = LocalMailer()

    private val api: TokensAPI = TokensAPI(tokensStorage, usersStorage, verificationsStorage, mailer)

    private val user by lazy { runBlocking { usersStorage.create(EMAIL) } }

    @Test
    fun create(): Unit = runBlocking {
        assert(api.create(user.email).isSuccess())
    }

    @Test
    fun getTokens(): Unit = runBlocking {
        tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), 0, TokenType.REGULAR)
        tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), 0, TokenType.REGULAR)
        api.getTokens(user).also {
            assert(it.isSuccess())
        }.also {
            assert(it.valueOrThrow().isNotEmpty())
        }
    }

    @Test
    fun unauthorizeMe() = runBlocking {
        val token =
            tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), 0, TokenType.REGULAR)
        assert(api.unauthorize(token.token).isSuccess())
    }

    @Test
    fun unauthorize() = runBlocking {
        val token =
            tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), 0, TokenType.REGULAR)
        assert(api.unauthorize(user, token.tokenId).isSuccess())
    }

    @Test
    fun getToken() = runBlocking {
        val token =
            tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), 0, TokenType.REGULAR)
        assert(api.getToken(token.token).isSuccess())
    }

}