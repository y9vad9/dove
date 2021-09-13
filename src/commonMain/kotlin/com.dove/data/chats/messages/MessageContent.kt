package com.dove.data.chats.messages

import com.dove.data.files.FileInfo

sealed interface MessageContent<T> {
    val value: T
    val type: MessageType

    /**
     * Simple plain text message.
     * @param value - text message.
     */
    class PlainText(override val value: String) : MessageContent<String> {
        override val type: MessageType = MessageType.TEXT

        override fun toString(): String = value
    }

    /**
     * Media message (video / audio / image)
     * @param value - media file information (url, file name, id).
     */
    class Media(override val value: FileInfo) : MessageContent<FileInfo> {
        override val type: MessageType = MessageType.MEDIA

        override fun toString(): String = value.uuid
    }

    /**
     * Any other file message.
     * @param value - file information (url, file name, id).
     */
    class File(override val value: FileInfo) : MessageContent<FileInfo> {
        override val type: MessageType = MessageType.FILE

        override fun toString(): String = value.uuid
    }

}