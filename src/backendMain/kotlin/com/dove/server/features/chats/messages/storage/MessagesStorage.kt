package com.dove.server.features.chats.messages.storage

import com.dove.data.chats.messages.Message
import com.dove.data.chats.messages.MessageContent
import org.jetbrains.annotations.TestOnly

interface MessagesStorage {

    companion object Default : MessagesStorage by DatabaseMessagesStorage

    /**
     * Creates new message in chat with [chatId].
     * @param messageOwner - id of user that sent message.
     * @param chatId - unique chat identifier.
     * @param message - message to send.
     */
    suspend fun create(
        messageOwner: Long,
        chatId: Long,
        message: MessageContent<*>
    )

    /**
     * Gets [number] messages with [offset].
     * @param number - number of messages to get.
     * @param offset - message getting offset.
     * @return [List] of [Message].
     */
    suspend fun readAll(chatId: Long, number: Int, offset: Long): List<Message>

    /**
     * Gets message by [messageId].
     */
    suspend fun read(messageId: Long): Message?

    /**
     * Deletes message with id [messageId].
     */
    suspend fun delete(messageId: Long)

    /**
     * Deletes everything in storage.
     */
    @TestOnly
    suspend fun deleteAll()
}