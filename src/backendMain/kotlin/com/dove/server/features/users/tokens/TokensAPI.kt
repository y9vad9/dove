package com.dove.server.features.users.tokens

import com.dove.data.Constants
import com.dove.data.api.ApiResult
import com.dove.data.api.errors.InternalApiError
import com.dove.data.api.errors.InvalidTokenError
import com.dove.data.monad.Either
import com.dove.data.users.tokens.Token
import com.dove.data.users.tokens.TokenType
import com.dove.data.users.verifications.VerificationType
import com.dove.mailer.Email
import com.dove.server.features.users.UsersStorage
import com.dove.server.features.users.verifications.VerificationsStorage
import com.dove.server.utils.emails.EmailSender
import com.dove.server.utils.random.nextString
import com.dove.server.utils.time.timeInMs
import kotlin.random.Random

object TokensAPI {
    suspend fun create(
        email: String
    ): ApiResult<Unit> {
        val code = Random.nextString(Constants.CODE_LENGTH)
        val user = UsersStorage.read(email)
        VerificationsStorage.create(
            email,
            code,
            if (user != null)
                VerificationType.AUTH
            else VerificationType.REGISTRATION,
            timeInMs
        )

        val status = EmailSender.send(
            Email(
                "Verify your authorization",
                "Your code for dove is $code.",
                email
            )
        )

        return if (status)
            ApiResult.success(Unit)
        else ApiResult.error(InternalApiError)
    }

    suspend fun getToken(token: String): ApiResult<Token> {
        return TokensStorage.read(token)
            ?.let(Either.Companion::success)
            ?: Either.error(InvalidTokenError())
    }

    suspend fun getTokens(token: String): ApiResult<List<Token>> {
        val tokenObject = TokensStorage.read(token) ?: return Either.error(InvalidTokenError())

        if (tokenObject.type != TokenType.MANAGING)
            return Either.error(InvalidTokenError("Token type is invalid."))

        return Either.success(TokensStorage.readAll(tokenObject.userId))
    }

    suspend fun unauthorize(token: String): ApiResult<Unit> {
        val auth = TokensStorage.read(token) ?: Either.error(InvalidTokenError())
        return Either.success(TokensStorage.delete(token))
    }

    suspend fun unauthorize(authorizationToken: String, tokenToUnauth: Long): ApiResult<Unit> {
        val auth = TokensStorage.read(authorizationToken) ?: return Either.error(InvalidTokenError())

        if (auth.type != TokenType.MANAGING)
            return Either.error(InvalidTokenError("Token type is invalid."))

        val tokenToRemove = TokensStorage.read(tokenToUnauth)
            ?: return Either.error(InvalidTokenError("Token to remove is not found."))

        return if (tokenToRemove.userId == auth.userId)
            Either.success(TokensStorage.delete(tokenToRemove.tokenId))
        else Either.error(InvalidTokenError("Token to remove is not found."))
    }

}