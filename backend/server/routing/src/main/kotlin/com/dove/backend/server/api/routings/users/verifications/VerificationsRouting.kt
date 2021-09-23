package com.dove.backend.server.api.routings.users.verifications

import com.dove.backend.apis.users.verifications.VerificationsAPI
import com.dove.backend.server.api.routings.Tags
import com.dove.data.users.tokens.Token
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import com.y9neon.openapi.get

fun NormalOpenAPIRoute.tokenVerifications(
    api: VerificationsAPI
) = tag(Tags.Confirmations).route("/verifications") {

    data class VerifyAuthRequest(
        @QueryParam("User email")
        val email: String,
        @QueryParam("Received code")
        val code: String
    )

    fun NormalOpenAPIRoute.verifyAuthRequest() {
        get<VerifyAuthRequest, Token> {
            api.confirmAuth(email, code)
        }
    }

    verifyAuthRequest()
}