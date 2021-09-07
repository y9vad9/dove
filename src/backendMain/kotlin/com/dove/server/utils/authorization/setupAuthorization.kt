package com.dove.server.utils.authorization

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.InvalidTokenError
import com.dove.data.monad.Either
import com.dove.data.monad.isSuccess
import com.dove.data.monad.map
import com.dove.data.monad.valueOrNull
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.users.UsersAPI
import com.dove.server.features.users.tokens.TokensAPI
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.setupAuthorization() {
    install(UserAuthorization) {
        authorize {
            val token = call.request.headers["Authorization"]
                ?.takeIf { it.matches(Regex("Bearer .+")) }
                ?.split(" ", limit = 2)
                ?.getOrNull(index = 1)
                ?: return@authorize ApiResult.error(InvalidTokenError())

            return@authorize TokensAPI.getToken(token)
                .map {
                    if (it.isSuccess() && it.value.type != TokenType.REGULAR)
                        Either.error(InvalidTokenError())
                    else it
                }
                .valueOrNull()
                ?.let { UsersAPI.getById(it.userId) }
                ?: ApiResult.error(InvalidTokenError())
        }
        catch { error ->
            call.respond(HttpStatusCode.Unauthorized, error)
        }
    }
}