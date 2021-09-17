package com.dove.server.features.users.tokens

import com.dove.data.Constants
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrThrow
import com.dove.data.users.tokens.TokenType
import com.dove.mailer.LocalMailer
import com.dove.server.features.users.storage.MockedUsersStorage
import com.dove.server.features.users.storage.UsersStorage
import com.dove.server.features.users.tokens.storage.MockedTokensStorage
import com.dove.server.features.users.tokens.storage.TokensStorage
import com.dove.server.features.users.verifications.storage.MockedVerificationsStorage
import com.dove.server.features.users.verifications.storage.VerificationsStorage
import com.dove.server.utils.random.nextString
import com.dove.server.utils.time.timeInMs
import kotlinx.coroutines.runBlocking
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
        tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        api.getTokens(user).also {
            assert(it.isSuccess())
        }.also {
            assert(it.valueOrThrow().isNotEmpty())
        }
    }

    @Test
    fun unauthorizeMe() = runBlocking {
        val token =
            tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        assert(api.unauthorize(token.token).isSuccess())
    }

    @Test
    fun unauthorize() = runBlocking {
        val token =
            tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        assert(api.unauthorize(user, token.tokenId).isSuccess())
    }

    @Test
    fun getToken() = runBlocking {
        val token =
            tokensStorage.create(user.id, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        assert(api.getToken(token.token).isSuccess())
    }

}