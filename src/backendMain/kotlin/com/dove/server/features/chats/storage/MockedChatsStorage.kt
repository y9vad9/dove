package com.dove.server.features.chats.storage

import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import com.dove.server.features.chats.members.storage.ChatMembersStorage
import com.dove.server.features.users.storage.UsersStorage
import kotlin.random.Random

class MockedChatsStorage(private val membersStorage: ChatMembersStorage, private val usersStorage: UsersStorage) :
    ChatsStorage {
    private data class ChatModel(
        val id: Long,
        val chatName: String?,
        val chatImage: String?,
        val chatType: ChatType
    )

    private val chats: MutableList<ChatModel> = mutableListOf()

    override suspend fun create(chatName: String?, chatImage: String?, chatType: ChatType): Long {
        val chatId = Random.nextLong(1000)
        chats += ChatModel(chatId, chatName, chatImage, chatType)
        return chatId
    }

    override suspend fun update(chatId: Long, newChatName: String?, newChatImage: String?) {
        val index = chats.indexOfFirst { it.id == chatId }
        val chatModel = chats[index]
        chats[index] = chatModel.copy(
            chatName = newChatName ?: chatModel.chatName,
            chatImage = newChatImage ?: chatModel.chatImage
        )
    }

    override suspend fun readAll(userId: Long, number: Int, offset: Long): List<Chat> {
        return membersStorage.chatIdsUserExistIn(userId, number, offset).map { chatId ->
            read(chatId, userId) ?: error("Error while converting chat with id $chatId for user with id $userId")
        }
    }

    override suspend fun read(chatId: Long, userId: Long): Chat? {
        return chats.firstOrNull { it.id == chatId }?.toChat(chatId, userId)
    }

    override suspend fun delete(chatId: Long) {
        chats.removeIf { it.id == chatId }
    }

    override suspend fun deleteAll() {
        chats.clear()
    }

    private suspend fun ChatModel.toChat(chatId: Long, userId: Long) = when (chatType) {
        ChatType.GROUP -> Chat.Group(
            chatId,
            chatName!!,
            chatImage
        )
        ChatType.PERSONAL -> Chat.Personal(
            chatId,
            usersStorage.read(membersStorage.readAll(chatId, 20, 0).single { it.memberId != userId }.memberId)!!
        )
    }
}