package com.dove.server.features.chats

import com.dove.data.Constants
import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import com.dove.server.di.DatabaseDI
import com.dove.server.features.chats.members.ChatMembersStorage
import com.dove.server.features.users.UsersStorage
import com.dove.server.utils.time.timeInMs
import org.jetbrains.annotations.TestOnly
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object ChatsStorage {
    private object Chats : Table() {
        val CHAT_ID: Column<Long> = long("chat_id").autoIncrement()
        val CHAT_NAME: Column<String?> = varchar("chat_name", Constants.CHAT_NAME_MAX_LEN).nullable()
        val CHAT_IMAGE: Column<String?> = varchar("chat_image", 32).nullable()
        val CHAT_TYPE: Column<ChatType> = enumeration("chat_type", ChatType::class)
        val CHAT_CREATION_TIME: Column<Long> = long("chat_creation_time").clientDefault { timeInMs }
    }

    init {
        transaction(DatabaseDI.database) {
            SchemaUtils.create(Chats)
        }
    }

    /**
     * Creates new chat in database.
     * @param chatName - name of chat (only for [ChatType.GROUP])
     * @param chatImage - image uuid of chat (only for [ChatType.GROUP])
     * @param chatType - type of chat.
     */
    suspend fun create(chatName: String?, chatImage: String?, chatType: ChatType): Long = newSuspendedTransaction {
        Chats.insert {
            it[CHAT_NAME] = chatName
            it[CHAT_IMAGE] = chatImage
            it[CHAT_TYPE] = chatType
        }[Chats.CHAT_ID]
    }

    /**
     * Updates chat with [chatId].
     * @param newChatName - new chat name (if null it won't change anything)
     * @param newChatImage - new chat image (if null it won't change anything)
     */
    suspend fun update(chatId: Long, newChatName: String?, newChatImage: String?): Unit = newSuspendedTransaction {
        Chats.update(
            where = { Chats.CHAT_ID eq chatId },
            body = {
                if (newChatName != null)
                    it[CHAT_NAME] = newChatName
                if (newChatImage != null)
                    it[CHAT_IMAGE] = newChatImage
            }
        )
    }


    /**
     * Gets chats where user with id [userId] consists in.
     * @param userId - user identifier.
     * @param number - number of columns that will be loaded.
     * @param offset - position from which it will load.
     */
    suspend fun readAll(userId: Long, number: Int, offset: Long): List<Chat> {
        return ChatMembersStorage.chatIdsUserExistIn(userId, number, offset).map { id ->
            newSuspendedTransaction {
                Chats.select { Chats.CHAT_ID eq id }.first().toChat(userId)
            }
        }
    }

    /**
     * Gets chat with id [chatId].
     * @param chatId - chat identifier.
     * @param userId - user id (which gets chat, used for mapping).
     */
    suspend fun read(chatId: Long, userId: Long): Chat? = newSuspendedTransaction {
        Chats.select { Chats.CHAT_ID eq chatId }.singleOrNull()?.toChat(userId)
    }

    /**
     * Deletes chat with [chatId].
     */
    suspend fun delete(chatId: Long): Unit = newSuspendedTransaction {
        Chats.deleteWhere { Chats.CHAT_ID eq chatId }
    }

    /**
     * Deletes all chat in database.
     */
    @TestOnly
    suspend fun deleteAll(): Unit = newSuspendedTransaction {
        Chats.deleteAll()
    }

    private suspend fun ResultRow.toChat(userId: Long): Chat {
        val chatId = get(Chats.CHAT_ID)
        return when (get(Chats.CHAT_TYPE)) {
            ChatType.PERSONAL -> Chat.Personal(
                chatId,
                UsersStorage.read(
                    ChatMembersStorage.readAll(chatId, 2, 0)
                        .toMutableList()
                        .apply {
                            removeIf { it.memberId == userId }
                        }.first().memberId
                )!!
            )
            ChatType.GROUP -> Chat.Group(
                chatId,
                get(Chats.CHAT_NAME) ?: "Unknown",
                get(Chats.CHAT_IMAGE)
            )
        }
    }

}