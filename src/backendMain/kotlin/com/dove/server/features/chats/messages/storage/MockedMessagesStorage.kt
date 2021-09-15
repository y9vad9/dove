package com.dove.server.features.chats.messages.storage

import com.dove.data.chats.messages.Message
import com.dove.data.chats.messages.MessageContent
import com.dove.data.users.User
import com.dove.server.utils.time.timeInMs
import kotlin.random.Random

class MockedMessagesStorage : MessagesStorage {
    private val messages: MutableMap<Long, List<Message>> = mutableMapOf()

    override suspend fun create(messageOwner: Long, chatId: Long, message: MessageContent<*>) {
        val chatMessages = messages[chatId] ?: listOf()
        messages[chatId] = chatMessages + Message(
            Random.nextLong(),
            User(messageOwner, "", null, ""),
            message,
            timeInMs
        )
    }

    override suspend fun readAll(chatId: Long, number: Int, offset: Long): List<Message> {
        return (messages[chatId] ?: return emptyList()).subList(offset.toInt(), offset.toInt() + number)
    }

    override suspend fun read(messageId: Long): Message? {
        messages.toList().forEach { pair ->
            return pair.second.firstOrNull { it.messageId == messageId } ?: return@forEach
        }
        return null
    }

    override suspend fun delete(messageId: Long) {
        val chatWithMessage = messages.toList().first { messageId in it.second.map(Message::messageId) }
        messages[chatWithMessage.first] = chatWithMessage.second.filter { it.messageId != messageId }
    }

    override suspend fun deleteAll() {
        messages.clear()
    }
}