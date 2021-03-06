package com.dove.backend.apis.users.verifications

import com.dove.backend.features.time.timeInMs
import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.core.users.tokens.TokensStorage
import com.dove.backend.storage.core.users.verifications.VerificationsStorage
import com.dove.data.Constants
import com.dove.data.api.ApiError.Companion.InvalidVerificationError
import com.dove.data.api.ApiResult
import com.dove.data.users.tokens.Token
import com.dove.data.users.tokens.TokenType
import com.dove.data.users.verifications.VerificationType
import nextString
import kotlin.random.Random

class VerificationsAPI(
    private val verificationsStorage: VerificationsStorage,
    private val usersStorage: UsersStorage,
    private val tokensStorage: TokensStorage
) {
    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    suspend fun confirmAuth(email: String, code: String): ApiResult<Token> {
        val verification = verificationsStorage.read(email, code, null)
            ?: return ApiResult.error(InvalidVerificationError)
        val user = usersStorage.create(email)
        val token = Random.nextString(Constants.TOKEN_LENGTH)
        return when (verification.type) {
            VerificationType.AUTH -> {
                ApiResult.success(tokensStorage.create(user.id, token, timeInMs, TokenType.REGULAR))
            }
            VerificationType.REGISTRATION -> {
                val newUser = usersStorage.create(email)
                ApiResult.success(tokensStorage.create(newUser.id, token, timeInMs, TokenType.REGISTRATION))
            }
            else -> ApiResult.error(InvalidVerificationError)
        }
    }
}