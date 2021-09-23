package com.dove.backend.server.main.setup

import io.ktor.application.*
import io.ktor.websocket.*

fun Application.setupSockets() {
    install(WebSockets) {

    }
}