package com.dove.data.chats

import com.dove.data.users.User

sealed interface Chat {
    val chatId: Long
    val type: ChatType

    class Personal(override val chatId: Long, val companion: User) : Chat {
        override val type: ChatType = ChatType.PERSONAL
    }

    class Group(
        override val chatId: Long,
        chatName: String,
        chatImage: String?
    ) : Chat, GroupInfo(chatName, chatImage) {
        override val type: ChatType = ChatType.GROUP
    }

}