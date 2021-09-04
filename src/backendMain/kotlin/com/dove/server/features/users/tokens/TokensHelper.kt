@file:Suppress("MemberVisibilityCanBePrivate")

package com.dove.server.features.users.tokens

import com.dove.data.api.errors.InvalidTokenError
import com.dove.data.monad.Either
import com.dove.data.monad.isSuccess
import com.dove.data.monad.map
import com.dove.data.users.tokens.Token
import com.dove.data.users.tokens.TokenType

object TokensHelper {
    /**
     * Checks is authorization valid.
     */
    suspend fun checkAuthorization(token: String): Either<Token, InvalidTokenError> {
        val auth = TokensStorage.read(token) ?: return Either.error(InvalidTokenError())
        return Either.success(auth)
    }

    /**
     * Checks token validness (should exist and be [TokenType.REGULAR]).
     */
    suspend fun checkRegularAuthorization(token: String): Either<Token, InvalidTokenError> =
        checkAuthorization(token).map {
            if (it.isSuccess() && it.value.type != TokenType.REGULAR)
                Either.error(InvalidTokenError())
            else it
        }
}