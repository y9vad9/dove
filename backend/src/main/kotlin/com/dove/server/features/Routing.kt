package com.dove.server.features

import com.dove.server.features.chats.chats
import com.dove.server.features.files.files
import com.dove.server.features.users.tokens.tokens
import com.dove.server.features.users.users
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute

fun NormalOpenAPIRoute.routing() {
    users()
    tokens()
    files()
    chats()
}