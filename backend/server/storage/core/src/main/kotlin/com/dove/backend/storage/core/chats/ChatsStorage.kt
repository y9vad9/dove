package com.dove.backend.storage.core.chats

import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import org.jetbrains.annotations.TestOnly

interface ChatsStorage {
    /**
     * Creates new chat in database.
     * @param chatName - name of chat (only for [ChatType.GROUP])
     * @param chatImage - image uuid of chat (only for [ChatType.GROUP])
     * @param chatType - type of chat.
     */
    suspend fun create(chatName: String?, chatImage: String?, chatType: ChatType, time: Long): Long

    /**
     * Updates chat with [chatId].
     * @param newChatName - new chat name (if null it won't change anything)
     * @param newChatImage - new chat image (if null it won't change anything)
     */
    suspend fun update(chatId: Long, newChatName: String?, newChatImage: String?)

    /**
     * Gets chats where user with id [userId] consists in.
     * @param userId - user identifier.
     * @param number - number of columns that will be loaded.
     * @param offset - position from which it will load.
     */
    suspend fun readAll(userId: Long, number: Int, offset: Long): List<Chat>

    /**
     * Gets chat with id [chatId].
     * @param chatId - chat identifier.
     * @param userId - user id (which gets chat, used for mapping).
     */
    suspend fun read(chatId: Long, userId: Long): Chat?

    /**
     * Deletes chat with [chatId].
     */
    suspend fun delete(chatId: Long)

    /**
     * Deletes all chat in database.
     */
    @TestOnly
    suspend fun deleteAll()
}