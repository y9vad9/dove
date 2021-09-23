package com.dove.backend.apis.chats.messages

import com.dove.backend.storage.core.chats.ChatsStorage
import com.dove.backend.storage.core.chats.members.ChatMembersStorage
import com.dove.backend.storage.core.chats.messages.MessagesStorage
import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.mocked.chats.MockedChatsStorage
import com.dove.backend.storage.mocked.chats.members.MockedChatMembersStorage
import com.dove.backend.storage.mocked.chats.messages.MockedMessagesStorage
import com.dove.backend.storage.mocked.users.MockedUsersStorage
import com.dove.data.chats.ChatType
import com.dove.data.chats.MemberType
import com.dove.data.chats.messages.MessageContent
import com.dove.data.chats.messages.MessageType
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrThrow
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
        val chatId = chatsStorage.create("test", "", ChatType.GROUP, 0)
        chatMembersStorage.create(chatId, user.id, MemberType.REGULAR, 0)
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
        val chatId = chatsStorage.create("test", "", ChatType.GROUP, 0)
        chatMembersStorage.create(chatId, user.id, MemberType.REGULAR, 0)
        assert(api.sendMessage(user, chatId, "test", MessageType.TEXT).isSuccess())
    }

    @Test
    fun deleteMessages(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", "", ChatType.GROUP, 0)
        chatMembersStorage.create(chatId, user.id, MemberType.REGULAR, 0)
        messagesStorage.create(user.id, chatId, MessageContent.PlainText("test"))
        val id = messagesStorage.readAll(chatId, 20, 0).first().messageId
        assert(api.deleteMessage(user, chatId, id).isSuccess())
    }
}