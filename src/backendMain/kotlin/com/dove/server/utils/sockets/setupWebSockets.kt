package com.dove.server.utils.sockets

import com.dove.server.features.chats.chatsSocket
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.*

fun Application.setupSockets() {
    install(WebSockets) {

    }

    routing {
        chatsSocket()
    }
}