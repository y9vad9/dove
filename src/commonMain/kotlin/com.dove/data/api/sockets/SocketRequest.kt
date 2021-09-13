package com.dove.data.api.sockets

sealed interface SocketRequest {
    /**
     * Socket request type.
     * Used for serialization/deserialization process.
     */
    val type: SocketRequestType
}