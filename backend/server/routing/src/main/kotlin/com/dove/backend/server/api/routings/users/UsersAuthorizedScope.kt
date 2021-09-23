package com.dove.backend.server.api.routings.users

import com.dove.data.users.User
import com.papsign.ktor.openapigen.route.OpenAPIRoute
import com.y9neon.middleware.authorization.Authorization
import com.y9neon.openapi.authorization

class UsersAuthorizedScope<TRoute : OpenAPIRoute<TRoute>>(
    private val route: TRoute,
    private val feature: Authorization.Feature<User>
) {
    val user get() = route.authorization(feature)
}