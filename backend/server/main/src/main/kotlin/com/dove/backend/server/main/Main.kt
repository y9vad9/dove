package com.dove.backend.server.main

import com.dove.backend.server.api.routings.rootRouting
import com.dove.backend.server.main.extentions.authorization.UserAuthorization
import com.dove.backend.server.main.setup.setupAuthorization
import com.dove.backend.server.main.setup.setupSerialization
import com.dove.backend.server.main.setup.setupSockets
import com.dove.backend.server.sockets.routing.SocketRouting
import com.papsign.ktor.openapigen.route.apiRouting
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, port = Environment.port) {
        setupOpenApi()
        setupSerialization()
        setupAuthorization()
        setupSockets()

        apiRouting {
            rootRouting(ServerStorages, Environment.mailer, UserAuthorization)
        }

        routing {
            SocketRouting(ServerStorages.userTokensStorage, ServerStorages.chatMembersStorage).chatsSocket(this)
        }
    }.start(wait = true)
}

