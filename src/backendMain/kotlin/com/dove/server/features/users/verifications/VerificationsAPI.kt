package com.dove.server.features.users.verifications

import com.dove.data.Constants
import com.dove.data.api.ApiResult
import com.dove.data.api.errors.InvalidVerificationError
import com.dove.data.users.tokens.Token
import com.dove.data.users.tokens.TokenType
import com.dove.data.users.verifications.VerificationType
import com.dove.server.features.users.UsersStorage
import com.dove.server.features.users.tokens.TokensStorage
import com.dove.server.utils.random.nextString
import com.dove.server.utils.time.timeInMs
import kotlin.random.Random

object VerificationsAPI {
    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    suspend fun confirmAuth(email: String, code: String): ApiResult<Token> {
        val verification = VerificationsStorage.read(email, code, null)
            ?: return ApiResult.error(InvalidVerificationError)
        val user = UsersStorage.create(email)
        val token = Random.nextString(Constants.TOKEN_LENGTH)
        return when (verification.type) {
            VerificationType.AUTH -> {
                ApiResult.success(TokensStorage.create(user.id, token, timeInMs, TokenType.REGULAR))
            }
            VerificationType.REGISTRATION -> {
                val newUser = UsersStorage.create(email)
                ApiResult.success(TokensStorage.create(newUser.id, token, timeInMs, TokenType.REGISTRATION))
            }
            else -> ApiResult.error(InvalidVerificationError)
        }
    }
}