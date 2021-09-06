package com.dove.server.features.users.tokens

import com.dove.data.users.tokens.Token
import com.dove.server.features.Tags
import com.dove.server.utils.openapi.delete
import com.dove.server.utils.openapi.get
import com.papsign.ktor.openapigen.annotations.parameters.HeaderParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.tag

fun NormalOpenAPIRoute.tokens() = tag(Tags.Tokens) {
    createToken()
    getTokenRequest()
    getTokensRequest()
    unauthorizeMe()
    unauthorize()
}

private data class CreateTokenRequest(
    @QueryParam("Email")
    val email: String
)

private fun NormalOpenAPIRoute.createToken() {
    get<CreateTokenRequest, Unit> {
        TokensAPI.create(email)
    }
}

data class AuthorizeByTokenRequest(
    @HeaderParam("Authorization token")
    val token: String
)

private fun NormalOpenAPIRoute.getTokenRequest() = get<AuthorizeByTokenRequest, Token> {
    TokensAPI.getToken(token)
}

private fun NormalOpenAPIRoute.getTokensRequest() = get<AuthorizeByTokenRequest, List<Token>> {
    TokensAPI.getTokens(token)
}

private fun NormalOpenAPIRoute.unauthorizeMe() = delete<AuthorizeByTokenRequest, Unit> {
    TokensAPI.unauthorize(token)
}

private data class UnauthorizeRequest(
    @HeaderParam("Authorization token")
    val token: String,
    @QueryParam("Token to unauthorize.")
    val unauthTokenId: Long
)

private fun NormalOpenAPIRoute.unauthorize() = delete<UnauthorizeRequest, Unit> {
    TokensAPI.unauthorize(token, unauthTokenId)
}


