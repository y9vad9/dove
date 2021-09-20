package com.dove.data.chats.messages

import com.dove.data.files.FileInfo
import kotlinx.serialization.SerialName

fun MessageContent(message: String, type: MessageType): MessageContent {
    return when (type) {
        MessageType.TEXT -> MessageContent.PlainText(message)
        MessageType.MEDIA -> MessageContent.Media(FileInfo(message, "", "", 0, 0))
        MessageType.FILE -> MessageContent.File(FileInfo(message, "", "", 0, 0))
    }
}

sealed interface MessageContent {

    /**
     * Simple plain text message.
     * @param text - text message.
     */
    @SerialName("PlainText")
    class PlainText(val text: String) : MessageContent {
        override fun toString(): String = text
    }

    /**
     * Media message (video / audio / image)
     * @param mediaInfo - media file information (url, file name, id).
     */
    @SerialName("Media")
    class Media(val mediaInfo: FileInfo) : MessageContent {
        override fun toString(): String = mediaInfo.uuid
    }

    /**
     * Any other file message.
     * @param fileInfo - file information (url, file name, id).
     */
    @SerialName("PlainText")
    class File(val fileInfo: FileInfo) : MessageContent {
        override fun toString(): String = fileInfo.uuid
    }

}