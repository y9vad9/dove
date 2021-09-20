package com.dove.server.features.chats.messages

import com.dove.data.chats.ChatType
import com.dove.data.chats.MemberType
import com.dove.data.chats.messages.MessageContent
import com.dove.data.chats.messages.MessageType
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrThrow
import com.dove.server.features.chats.members.storage.ChatMembersStorage
import com.dove.server.features.chats.members.storage.MockedChatMembersStorage
import com.dove.server.features.chats.messages.storage.MessagesStorage
import com.dove.server.features.chats.messages.storage.MockedMessagesStorage
import com.dove.server.features.chats.storage.ChatsStorage
import com.dove.server.features.chats.storage.MockedChatsStorage
import com.dove.server.features.users.storage.MockedUsersStorage
import com.dove.server.features.users.storage.UsersStorage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable

@Testable
object ChatMessagesAPITest {
    private val usersStorage: UsersStorage = MockedUsersStorage()
    private val chatMembersStorage: ChatMembersStorage = MockedChatMembersStorage()
    private val messagesStorage: MessagesStorage = MockedMessagesStorage()
    private val chatsStorage: ChatsStorage = MockedChatsStorage(chatMembersStorage, usersStorage)

    private val api: MessagesAPI = MessagesAPI(messagesStorage, chatMembersStorage)

    private val user by lazy { runBlocking { usersStorage.create("test@testing.email") } }

    @Test
    fun getMessages(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", "", ChatType.GROUP)
        chatMembersStorage.create(chatId, user.id, MemberType.REGULAR)
        repeat(10) { time ->
            val another = usersStorage.create("$time@mail.com")
            messagesStorage.create(another.id, chatId, MessageContent.PlainText("test $time"))
        }
        api.getMessages(user, chatId, 20, 0).apply {
            assert(isSuccess())
            assert(valueOrThrow().isNotEmpty())
        }
    }

    @Test
    fun sendMessage(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", "", ChatType.GROUP)
        chatMembersStorage.create(chatId, user.id, MemberType.REGULAR)
        assert(api.sendMessage(user, chatId, "test", MessageType.TEXT).isSuccess())
    }

    @Test
    fun deleteMessages(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", "", ChatType.GROUP)
        chatMembersStorage.create(chatId, user.id, MemberType.REGULAR)
        messagesStorage.create(user.id, chatId, MessageContent.PlainText("test"))
        val id = messagesStorage.readAll(chatId, 20, 0).first().messageId
        assert(api.deleteMessage(user, chatId, id).isSuccess())
    }
}