package com.dove.data.chats.messages

import kotlinx.serialization.Serializable

@Serializable
enum class MediaType {
    IMAGE, VIDEO, AUDIO
}