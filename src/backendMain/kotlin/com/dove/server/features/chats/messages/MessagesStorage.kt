package com.dove.server.features.chats.messages

import com.dove.data.Constants
import com.dove.data.chats.messages.FileInfo
import com.dove.data.chats.messages.Message
import com.dove.data.chats.messages.MessageContent
import com.dove.data.chats.messages.MessageType
import com.dove.data.users.User
import com.dove.server.di.DatabaseDI
import com.dove.server.features.files.FilesStorage
import com.dove.server.features.users.UsersStorage
import com.dove.server.utils.time.timeInMs
import org.jetbrains.annotations.TestOnly
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object MessagesStorage {
    private object Messages : Table() {
        val MESSAGE_ID = long("message_id").autoIncrement()
        val MESSAGE_OWNER = long("message_owner")
        val CHAT_ID = long("chat_id")
        val CONTENT = varchar("content", Constants.MESSAGE_CONTENT_MAX_LEN)
        val CONTENT_TYPE = enumeration("content_type", MessageType::class)
        val TIME = long("time").clientDefault { timeInMs }
    }

    init {
        transaction(DatabaseDI.database) {
            SchemaUtils.create(Messages)
        }
    }

    /**
     * Creates new message in chat with [chatId].
     * @param messageOwner - id of user that sent message.
     * @param chatId - unique chat identifier.
     * @param content - text message (or text representation of media/file).
     * @param type - message type.
     */
    suspend fun create(
        messageOwner: Long,
        chatId: Long,
        content: String,
        type: MessageType
    ): Unit = newSuspendedTransaction {
        Messages.insert {
            it[MESSAGE_OWNER] = messageOwner
            it[CHAT_ID] = chatId
            it[CONTENT] = content
            it[CONTENT_TYPE] = type
        }
    }

    /**
     * Gets [number] messages with [offset].
     * @param number - number of messages to get.
     * @param offset - message getting offset.
     * @return [List] of [Message].
     */
    suspend fun readAll(chatId: Long, number: Int, offset: Long): List<Message> = newSuspendedTransaction {
        val messages = Messages.select { Messages.CHAT_ID eq chatId }
            .limit(number, offset)
            .toList()
        val userIds = messages.map { it[Messages.MESSAGE_OWNER] }
        val users = UsersStorage.readAll(userIds)
        val files = FilesStorage.readAll(
            messages.filter {
                val contentType = it[Messages.CONTENT_TYPE]
                contentType == MessageType.MEDIA || contentType == MessageType.FILE
            }.map { it[Messages.CONTENT] })
        return@newSuspendedTransaction messages.map {
            it.toMessage(users, files)
        }
    }

    /**
     * Gets message by [messageId].
     */
    suspend fun read(messageId: Long): Message? = newSuspendedTransaction {
        Messages.select { Messages.MESSAGE_ID eq messageId }.firstOrNull()?.toMessage()
    }

    /**
     * Deletes message with id [messageId].
     */
    suspend fun delete(messageId: Long): Unit = newSuspendedTransaction {
        Messages.deleteWhere { Messages.MESSAGE_ID eq messageId }
    }

    @TestOnly
    suspend fun deleteAll(): Unit = newSuspendedTransaction {
        Messages.deleteAll()
    }

    /**
     * Converts [ResultRow] into [Message] with preloaded [users] and [files].
     */
    private fun ResultRow.toMessage(users: List<User>, files: List<FileInfo>): Message {
        val type = get(Messages.CONTENT_TYPE)
        val message = get(Messages.CONTENT)
        val content = when (type) {
            MessageType.TEXT -> MessageContent.PlainText(message)
            MessageType.MEDIA -> MessageContent.Media(files.first { it.uuid == message })
            MessageType.FILE -> MessageContent.File(files.first { it.uuid == message })
        }
        return Message(
            get(Messages.MESSAGE_ID), users.first { it.id == get(Messages.MESSAGE_OWNER) },
            content, get(Messages.TIME)
        )
    }

    /**
     * Converts [ResultRow] to [Message] by getting values from another storages.
     */
    private suspend fun ResultRow.toMessage(): Message {
        val type = get(Messages.CONTENT_TYPE)
        val message = get(Messages.CONTENT)
        val content = when (type) {
            MessageType.TEXT -> MessageContent.PlainText(message)
            MessageType.MEDIA -> MessageContent.Media(FilesStorage.read(UUID.fromString(message))!!)
            MessageType.FILE -> MessageContent.File(FilesStorage.read(UUID.fromString(message))!!)
        }
        return Message(
            get(Messages.MESSAGE_ID), UsersStorage.read(get(Messages.MESSAGE_OWNER))!!,
            content, get(Messages.TIME)
        )
    }
}