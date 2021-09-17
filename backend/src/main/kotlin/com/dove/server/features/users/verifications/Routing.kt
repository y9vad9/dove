package com.dove.server.features.users.verifications

import com.dove.data.users.tokens.Token
import com.dove.server.features.Tags
import com.dove.server.features.users.storage.UsersStorage
import com.dove.server.features.users.tokens.storage.TokensStorage
import com.dove.server.features.users.verifications.storage.VerificationsStorage
import com.dove.server.utils.openapi.get
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag

private val api: VerificationsAPI =
    VerificationsAPI(VerificationsStorage.Default, UsersStorage.Default, TokensStorage.Default)

fun NormalOpenAPIRoute.tokenVerifications() = tag(Tags.Confirmations).route("/verifications") {
    verifyAuthRequest()
}

private data class VerifyAuthRequest(
    @QueryParam("User email")
    val email: String,
    @QueryParam("Received code")
    val code: String
)

private fun NormalOpenAPIRoute.verifyAuthRequest() {
    get<VerifyAuthRequest, Token> {
        api.confirmAuth(email, code)
    }
}