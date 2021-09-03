package com.dove.server.utils.serialization

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json

fun Application.setupSerialization() {
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
        })
    }
}