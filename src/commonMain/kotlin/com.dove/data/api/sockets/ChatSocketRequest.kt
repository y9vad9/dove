package com.dove.data.api.sockets

import kotlinx.serialization.SerialName

/**
 * The root of any chat socket request.
 */
sealed interface ChatSocketRequest : SocketRequest {

    /**
     * Authorization token.
     */
    val token: String

    /**
     * Used for subscribing to chat changes.
     */
    @SerialName("Subscribe")
    class Subscribe(val chatsIds: Set<Long>, override val token: String) : ChatSocketRequest {
        override val type: SocketRequestType = SocketRequestType.SUBSCRIBE
    }

    /**
     * Used for unsubscribing from some chat changes.
     */
    @SerialName("Unsubscribe")
    class Unsubscribe(val chatsIds: Set<Long>, override val token: String) : ChatSocketRequest {
        override val type: SocketRequestType = SocketRequestType.UNSUBSCRIBE
    }
}