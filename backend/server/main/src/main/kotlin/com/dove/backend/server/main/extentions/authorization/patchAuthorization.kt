package com.dove.backend.server.main.extentions.authorization

import com.dove.data.users.User
import com.y9neon.middleware.authorization.Authorization
import kotlinx.serialization.json.*

val UserAuthorization = Authorization.Feature<User>()

fun JsonObject.patchAuthorization(): JsonObject {
    val map = toMutableMap()

    val components = map["components"]!!.jsonObject.toMutableMap()
    components["securitySchemes"] = buildJsonObject {
        put(
            key = "Auth",
            buildJsonObject {
                put("type", "http")
                put("scheme", "bearer")
            }
        )
    }
    map["components"] = Json.encodeToJsonElement(components)


    map["security"] = buildJsonArray {
        add(buildJsonObject {
            put("Auth", buildJsonArray { })
        })
    }

    return Json.encodeToJsonElement(map) as JsonObject
}