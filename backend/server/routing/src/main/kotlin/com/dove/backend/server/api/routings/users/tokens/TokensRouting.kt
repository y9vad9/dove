package com.dove.backend.server.api.routings.users.tokens

import com.dove.backend.apis.users.tokens.TokensAPI
import com.dove.backend.server.api.routings.Tags
import com.dove.backend.server.api.routings.users.UsersAuthorizedScope
import com.dove.data.users.User
import com.dove.data.users.tokens.Token
import com.papsign.ktor.openapigen.annotations.parameters.HeaderParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import com.y9neon.middleware.authorization.Authorization
import com.y9neon.openapi.delete
import com.y9neon.openapi.get
import com.y9neon.openapi.post

internal fun NormalOpenAPIRoute.tokens(
    api: TokensAPI,
    authFeature: Authorization.Feature<User>
) = route("tokens").tag(Tags.Tokens) {
    fun userAuthorized(block: UsersAuthorizedScope<NormalOpenAPIRoute>.() -> Unit) {
        block(UsersAuthorizedScope(this, authFeature))
    }

    data class CreateTokenRequest(
        @QueryParam("Email")
        val email: String
    )

    fun NormalOpenAPIRoute.createToken() {
        post<CreateTokenRequest, Unit, Unit> {
            api.create(email)
        }
    }

    data class AuthorizeByTokenRequest(
        @HeaderParam("Authorization token")
        val token: String
    )

    fun NormalOpenAPIRoute.getTokenRequest() = get<AuthorizeByTokenRequest, Token> {
        api.getToken(token)
    }

    fun NormalOpenAPIRoute.getTokensRequest() = userAuthorized {
        get<AuthorizeByTokenRequest, List<Token>> {
            api.getTokens(user)
        }
    }

    fun NormalOpenAPIRoute.unauthorizeMe() = route("me").delete<AuthorizeByTokenRequest, Unit> {
        api.unauthorize(token)
    }

    data class UnauthorizeRequest(
        @QueryParam("Token to unauthorize.")
        val unauthTokenId: Long
    )

    fun NormalOpenAPIRoute.unauthorize() = userAuthorized {
        delete<UnauthorizeRequest, Unit> {
            api.unauthorize(user, unauthTokenId)
        }
    }

    createToken()
    getTokenRequest()
    getTokensRequest()
    unauthorizeMe()
    unauthorize()
}