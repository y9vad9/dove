package com.dove.data.chats.messages

import com.dove.data.users.User

data class Message(
    val messageId: Long,
    val user: User,
    val content: MessageContent<*>,
    val time: Long
)