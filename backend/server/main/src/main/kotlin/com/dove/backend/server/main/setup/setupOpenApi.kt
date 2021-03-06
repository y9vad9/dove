package com.dove.backend.server.main

import com.dove.backend.server.main.extentions.authorization.patchAuthorization
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.schema.namer.DefaultSchemaNamer
import com.papsign.ktor.openapigen.schema.namer.SchemaNamer
import com.y9neon.openapi.kserialize
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.KType

fun Application.setupOpenApi() {
    install(OpenAPIGen) {
        info {
            version = "1.0.0"
            title = "Dove API"
            description = "Dove API Docs"

            contact {
                name = "Vadim Yaroschuk"
                url = "https://t.me/y9neon"
            }
        }

        if (Environment.debug) {
            server(url = "http://localhost:8080") {
                description = "Local API Endpoint"
            }
        }
        server(url = "https://dove.kotlingang.fun") {
            description = "Production API Endpoint"
        }
        replaceModule(DefaultSchemaNamer, object : SchemaNamer {
            override fun get(type: KType): String {
                return "$type".split(".").last()
            }
        })
    }

    routing {
        get(path = "/openapi.json") {
            val schema = (application.openAPIGen.api.kserialize() as JsonObject)
                .patchAuthorization()

            call.respond(schema)
        }
        get(path = "/") {
            call.respondRedirect(
                url = "/swagger-ui/index.html?url=/openapi.json",
                permanent = true
            )
        }
    }
}