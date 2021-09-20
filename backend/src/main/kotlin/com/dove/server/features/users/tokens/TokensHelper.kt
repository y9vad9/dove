@file:Suppress("MemberVisibilityCanBePrivate")

package com.dove.server.features.users.tokens

import com.dove.data.api.ApiError
import com.dove.data.monad.Either
import com.dove.data.monad.isSuccess
import com.dove.data.monad.map
import com.dove.data.users.tokens.Token
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.users.tokens.storage.DatabaseTokensStorage

object TokensHelper {
    /**
     * Checks is authorization valid.
     */
    suspend fun checkAuthorization(token: String): Either<Token, ApiError> {
        val auth = DatabaseTokensStorage.read(token) ?: return Either.error(ApiError.InvalidTokenError)
        return Either.success(auth)
    }

    /**
     * Checks token validness (should exist and be [TokenType.REGULAR]).
     */
    suspend fun checkRegularAuthorization(token: String): Either<Token, ApiError> =
        checkAuthorization(token).map {
            if (it.isSuccess() && it.value.type != TokenType.REGULAR)
                Either.error(ApiError.InvalidTokenError)
            else it
        }
}