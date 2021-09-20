package com.dove.server.features.users.tokens

import com.dove.data.users.tokens.Token
import com.dove.server.Environment
import com.dove.server.features.Tags
import com.dove.server.features.users.storage.UsersStorage
import com.dove.server.features.users.tokens.storage.TokensStorage
import com.dove.server.features.users.verifications.storage.VerificationsStorage
import com.dove.server.features.users.verifications.tokenVerifications
import com.dove.server.utils.openapi.*
import com.papsign.ktor.openapigen.annotations.parameters.HeaderParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag

private val api: TokensAPI =
    TokensAPI(TokensStorage.Default, UsersStorage.Default, VerificationsStorage.Default, Environment.mailer)

fun NormalOpenAPIRoute.tokens() = tag(Tags.Tokens).route("tokens") {
    createToken()
    getTokenRequest()
    getTokensRequest()
    unauthorizeMe()
    unauthorize()

    tokenVerifications()
}

private data class CreateTokenRequest(
    @QueryParam("Email")
    val email: String
)

private fun NormalOpenAPIRoute.createToken() {
    post<CreateTokenRequest, Unit, Unit> {
        api.create(email)
    }
}

data class AuthorizeByTokenRequest(
    @HeaderParam("Authorization token")
    val token: String
)

private fun NormalOpenAPIRoute.getTokenRequest() = get<AuthorizeByTokenRequest, Token> {
    api.getToken(token)
}

private fun NormalOpenAPIRoute.getTokensRequest() = userAuthorized {
    get<AuthorizeByTokenRequest, List<Token>> {
        api.getTokens(user)
    }
}

private fun NormalOpenAPIRoute.unauthorizeMe() = delete<AuthorizeByTokenRequest, Unit> {
    api.unauthorize(token)
}

private data class UnauthorizeRequest(
    @HeaderParam("Authorization token")
    val token: String,
    @QueryParam("Token to unauthorize.")
    val unauthTokenId: Long
)

private fun NormalOpenAPIRoute.unauthorize() = userAuthorized {
    delete<UnauthorizeRequest, Unit> {
        api.unauthorize(user, unauthTokenId)
    }
}


