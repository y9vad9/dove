package com.dove.data.chats.messages

import com.dove.data.files.FileInfo
import kotlinx.serialization.SerialName

sealed interface MessageContent<T> {
    val value: T

    /**
     * Simple plain text message.
     * @param value - text message.
     */
    @SerialName("PlainText")
    class PlainText(override val value: String) : MessageContent<String> {
        override fun toString(): String = value
    }

    /**
     * Media message (video / audio / image)
     * @param value - media file information (url, file name, id).
     */
    @SerialName("Media")
    class Media(override val value: FileInfo) : MessageContent<FileInfo> {
        override fun toString(): String = value.uuid
    }

    /**
     * Any other file message.
     * @param value - file information (url, file name, id).
     */
    @SerialName("PlainText")
    class File(override val value: FileInfo) : MessageContent<FileInfo> {
        override fun toString(): String = value.uuid
    }

}