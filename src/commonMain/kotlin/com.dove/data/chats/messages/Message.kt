package com.dove.data.chats.messages

import com.dove.data.users.User
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val messageId: Long,
    val user: User,
    @Contextual
    val content: MessageContent,
    val time: Long
)