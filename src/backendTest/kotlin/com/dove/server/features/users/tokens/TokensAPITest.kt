package com.dove.server.features.users.tokens

import com.dove.data.Constants
import com.dove.data.monad.isSuccess
import com.dove.data.users.User
import com.dove.data.users.tokens.TokenType
import com.dove.mailer.LocalMailer
import com.dove.server.features.users.UsersStorage
import com.dove.server.features.users.verifications.VerificationsAPI
import com.dove.server.features.users.verifications.VerificationsStorage
import com.dove.server.local.Environment
import com.dove.server.utils.random.nextString
import com.dove.server.utils.time.timeInMs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import kotlin.properties.Delegates
import kotlin.random.Random

@Testable
object TokensAPITest {

    private lateinit var mailer: LocalMailer
    private val userId get() = user.id
    private var user by Delegates.notNull<User>()

    @BeforeEach
    fun removeItems() = runBlocking {
        TokensStorage.deleteAll()
        VerificationsStorage.deleteAll()
    }

    @BeforeAll
    private fun createUser() = runBlocking {
        user = UsersStorage.create("test@mail.net")
    }

    @BeforeAll
    fun setupMailer() {
        mailer = Environment.mailer as LocalMailer
    }

    @Test
    fun `create and verify`() = runBlocking {
        assert(TokensAPI.create("test@test.com").isSuccess())
        val mailMessage = mailer.emails.first().message
        val code = mailMessage.substring(
            mailMessage.indexOf(".") - 6,
            mailMessage.indexOf(".") - 1
        )
        assert(VerificationsAPI.confirmAuth("test@test.com", code).isSuccess())
    }

    @Test
    fun getToken() = runBlocking {
        val token = Random.nextString(Constants.TOKEN_LENGTH)
        TokensStorage.create(1, token, timeInMs, TokenType.REGULAR)
        assert(TokensAPI.getToken(token).isSuccess())
    }

    @Test
    fun `get tokens by invalid token type`() = runBlocking {
        val token = Random.nextString(Constants.TOKEN_LENGTH)
        TokensStorage.create(1, token, timeInMs, TokenType.REGISTRATION)
        assert(!TokensAPI.getTokens(user).isSuccess())
    }

    @Test
    fun `get tokens by valid token type`() = runBlocking {
        val token = Random.nextString(Constants.TOKEN_LENGTH)
        TokensStorage.create(1, token, timeInMs, TokenType.REGULAR)
        assert(TokensAPI.getTokens(user).isSuccess())
    }

    @Test
    fun `unauthorize valid`(): Unit = runBlocking {
        val tokenOwnerString = Random.nextString(Constants.TOKEN_LENGTH)
        val tokenRegular = Random.nextString(Constants.TOKEN_LENGTH)
        val regularToken = TokensStorage.create(1, tokenRegular, timeInMs, TokenType.REGULAR)
        TokensStorage.create(user.id, tokenOwnerString, timeInMs, TokenType.REGULAR)
        assert(TokensAPI.unauthorize(user, regularToken.tokenId).isSuccess())
    }

    @Test
    fun `unauthorize invalid`(): Unit = runBlocking {
        val tokenNotOwnerString = Random.nextString(Constants.TOKEN_LENGTH)
        val tokenRegular = Random.nextString(Constants.TOKEN_LENGTH)
        val regularToken = TokensStorage.create(1, tokenRegular, timeInMs, TokenType.REGULAR)
        assert(TokensAPI.unauthorize(user, regularToken.tokenId).isSuccess())
    }

}