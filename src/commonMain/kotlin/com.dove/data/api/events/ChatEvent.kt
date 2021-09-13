package com.dove.data.api.events

import com.dove.data.chats.Chat
import com.dove.data.chats.messages.Message
import com.dove.data.users.User
import kotlinx.serialization.SerialName

sealed interface ChatEvent : Event {
    /**
     * Chat where everything happens.
     */
    val chat: Chat

    /**
     * When new message was received.
     */
    @SerialName("NewMessage")
    class NewMessage(override val user: User, override val chat: Chat, val message: Message) : ChatEvent

    /**
     * When user joined to chat.
     * @param user - user to notify.
     * @param chat - chat to which user joined.
     * @param joined - user that joined to [chat].
     */
    @SerialName("NewUserJoined")
    class NewUserJoined(override val user: User, override val chat: Chat, val joined: User) : ChatEvent

    /**
     * When some user left from chat.
     * @param user - user to notify.
     * @param chat - chat from which user left.
     * @param left - user that left.
     */
    @SerialName("UserLeft")
    class UserLeft(override val user: User, override val chat: Chat, val left: User) : ChatEvent
}