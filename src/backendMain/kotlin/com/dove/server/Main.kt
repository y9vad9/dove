package com.dove.server

import com.dove.server.features.routing
import com.dove.server.local.Environment
import com.dove.server.utils.authorization.setupAuthorization
import com.dove.server.utils.openapi.setupOpenApi
import com.dove.server.utils.serialization.setupSerialization
import com.papsign.ktor.openapigen.route.apiRouting
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, port = Environment.port) {
        setupOpenApi()
        setupSerialization()
        setupAuthorization()

        apiRouting {
            routing()
        }
    }
}

