package com.dove.server.utils.serialization

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*

fun Application.setupSerialization() {
    install(ContentNegotiation) {
        json(com.dove.utils.json.json)
    }
}