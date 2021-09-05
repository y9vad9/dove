package com.dove.server.features.users.tokens

import com.dove.data.monad.onError
import com.dove.data.monad.onSuccess
import com.dove.server.features.Tags
import com.dove.server.utils.openapi.respondError
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.tag

fun NormalOpenAPIRoute.tokens() = tag(Tags.Tokens) {
    createToken()
}

private data class CreateTokenRequest(
    @QueryParam("Email")
    val email: String
)

private fun NormalOpenAPIRoute.createToken() {
    get<CreateTokenRequest, Unit> { parameters ->
        TokensAPI.create(parameters.email).onSuccess {
            respond(it)
        }.onError {
            respondError(it)
        }
    }
}