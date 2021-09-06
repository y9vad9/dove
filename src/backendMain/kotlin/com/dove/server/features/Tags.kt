package com.dove.server.features

import com.papsign.ktor.openapigen.APITag

enum class Tags(override val description: String) : APITag {
    Users("Users API"),
    Tokens("Tokens API"),
    Chats("Chats API"),
    Messages("Messages API"),
    Members("Members API"),
    Files("Files API")
}