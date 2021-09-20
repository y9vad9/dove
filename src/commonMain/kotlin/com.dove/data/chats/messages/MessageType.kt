package com.dove.data.chats.messages

import kotlinx.serialization.Serializable

/**
 * Message types.
 */
@Serializable
enum class MessageType {
    /**
     * Marks that message has simple plain text.
     */
    TEXT,

    /**
     * Marks that message has image/audio/video inside.
     */
    MEDIA,

    /**
     * Marks that message has unknown type of file inside.
     */
    FILE
}