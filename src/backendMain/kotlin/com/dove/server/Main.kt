package com.dove.server

import com.dove.server.local.Environment
import com.dove.server.utils.openapi.setupOpenApi
import com.dove.server.utils.serialization.setupSerialization
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, port = Environment.port) {
        setupOpenApi()
        setupSerialization()
    }
}

