package com.dove.backend.server.main.setup

import com.dove.backend.apis.users.UsersAPI
import com.dove.backend.apis.users.tokens.TokensAPI
import com.dove.backend.server.main.Environment
import com.dove.backend.server.main.ServerStorages
import com.dove.backend.server.main.extentions.authorization.UserAuthorization
import com.dove.data.api.ApiError
import com.dove.data.api.ApiResult
import com.dove.data.monad.Either
import com.dove.data.monad.isSuccess
import com.dove.data.monad.map
import com.dove.data.monad.valueOrNull
import com.dove.data.users.tokens.TokenType
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
                ?: return@authorize ApiResult.error(ApiError.InvalidTokenError)

            val usersAPI = UsersAPI(ServerStorages.usersStorage)

            return@authorize TokensAPI(
                ServerStorages.userTokensStorage,
                ServerStorages.usersStorage,
                ServerStorages.verificationsStorage,
                Environment.mailer
            ).getToken(token)
                .map {
                    if (it.isSuccess() && it.value.type != TokenType.REGULAR)
                        Either.error(ApiError.InvalidTokenError)
                    else it
                }
                .valueOrNull()
                ?.let { usersAPI.getById(it.userId) }
                ?: ApiResult.error((ApiError.InvalidTokenError))
        }
        catch { error ->
            call.respond(HttpStatusCode.Unauthorized, error)
        }
    }
}