package com.dove.backend.server.api.routings.internal

import com.dove.data.users.User
import com.papsign.ktor.openapigen.route.OpenAPIRoute
import com.y9neon.middleware.authorization.Authorization
import com.y9neon.openapi.authorization
import com.y9neon.openapi.authorized
import io.ktor.util.pipeline.*

internal fun <TRoute : OpenAPIRoute<TRoute>> TRoute.getUser(feature: Authorization.Feature<User>) =
    authorization(feature)

@ContextDsl
internal inline fun <TRoute : OpenAPIRoute<TRoute>, R> TRoute.userAuthorized(
    feature: Authorization.Feature<User>,
    crossinline block: TRoute.(user: User) -> R
): TRoute = authorized(feature) {
    block(getUser(feature))
}