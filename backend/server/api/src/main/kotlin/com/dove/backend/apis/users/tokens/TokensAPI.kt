package com.dove.backend.apis.users.tokens

import com.dove.backend.features.time.timeInMs
import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.core.users.tokens.TokensStorage
import com.dove.backend.storage.core.users.verifications.VerificationsStorage
import com.dove.data.Constants
import com.dove.data.api.ApiError
import com.dove.data.api.ApiError.Companion.InvalidTokenError
import com.dove.data.api.ApiResult
import com.dove.data.monad.Either
import com.dove.data.users.User
import com.dove.data.users.tokens.Token
import com.dove.data.users.verifications.VerificationType
import com.dove.mailer.Email
import com.dove.mailer.Mailer
import nextString
import kotlin.random.Random

class TokensAPI(
    private val tokensStorage: TokensStorage,
    private val usersStorage: UsersStorage,
    private val verificationsStorage: VerificationsStorage,
    private val mailer: Mailer
) {
    suspend fun create(
        email: String
    ): ApiResult<Unit> {
        val code = Random.nextString(Constants.CODE_LENGTH)
        val user = usersStorage.read(email)
        verificationsStorage.create(
            email,
            code,
            if (user != null)
                VerificationType.AUTH
            else VerificationType.REGISTRATION,
            timeInMs
        )

        val status = mailer.send(
            Email(
                "Verify your authorization",
                "Your code for dove is $code.",
                email
            )
        )

        return if (status)
            ApiResult.success(Unit)
        else ApiResult.error(ApiError.InternalServerError)
    }

    suspend fun getToken(token: String): ApiResult<Token> {
        return tokensStorage.read(token)
            ?.let(Either.Companion::success)
            ?: Either.error(InvalidTokenError)
    }

    suspend fun getTokens(user: User): ApiResult<List<Token>> {
        return Either.success(tokensStorage.readAll(user.id))
    }

    suspend fun unauthorize(token: String): ApiResult<Unit> {
        tokensStorage.read(token) ?: Either.error(InvalidTokenError)
        return Either.success(tokensStorage.delete(token))
    }

    suspend fun unauthorize(user: User, tokenToUnauth: Long): ApiResult<Unit> {
        val tokenToRemove = tokensStorage.read(tokenToUnauth)
            ?: return Either.error(InvalidTokenError)

        return if (tokenToRemove.userId == user.id)
            Either.success(tokensStorage.delete(tokenToRemove.tokenId))
        else Either.error(InvalidTokenError)
    }

}