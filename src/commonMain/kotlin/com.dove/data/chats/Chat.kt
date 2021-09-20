package com.dove.data.chats

import com.dove.data.users.User
import kotlinx.serialization.SerialName

sealed interface Chat {
    val chatId: Long

    @SerialName("Personal")
    class Personal(override val chatId: Long, val companion: User) : Chat

    @SerialName("Group")
    class Group(
        override val chatId: Long,
        val name: String,
        val image: String?
    ) : Chat

}