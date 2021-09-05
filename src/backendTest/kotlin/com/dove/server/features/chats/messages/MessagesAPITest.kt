package com.dove.server.features.chats.messages

import com.dove.data.Constants
import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import com.dove.data.chats.MemberType
import com.dove.data.chats.messages.MessageContent
import com.dove.data.chats.messages.MessageType
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrThrow
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.chats.ChatsStorage
import com.dove.server.features.chats.members.ChatMembersStorage
import com.dove.server.features.users.UsersStorage
import com.dove.server.features.users.tokens.TokensStorage
import com.dove.server.utils.random.nextString
import com.dove.server.utils.time.timeInMs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import kotlin.properties.Delegates
import kotlin.random.Random

@Testable
object MessagesAPITest {

    private var userId by Delegates.notNull<Long>()
    private const val chatName: String = "Chat"
    private val token by lazy { Random.nextString(Constants.TOKEN_LENGTH) }

    @BeforeEach
    fun deleteItems() = runBlocking {
        MessagesStorage.deleteAll()
        TokensStorage.deleteAll()
        ChatMembersStorage.deleteAll()
    }

    @BeforeAll
    fun createUser() = runBlocking {
        userId = UsersStorage.create("test@email.com").id
    }

    private fun createChat(): Chat = runBlocking {
        val auth = TokensStorage.create(userId, token, timeInMs, TokenType.REGULAR)
        val chatId = ChatsStorage.create(chatName, null, ChatType.GROUP)
        ChatMembersStorage.create(chatId, auth.userId, MemberType.OWNER)
        ChatMembersStorage.create(chatId, Random.nextLong(Long.MAX_VALUE), MemberType.REGULAR)
        MessagesStorage.create(auth.userId, chatId, "test", MessageType.TEXT)
        return@runBlocking ChatsStorage.read(chatId, userId)!!
    }

    @Test
    fun getMessages() = runBlocking {
        val chat = createChat()
        val result = MessagesAPI.getMessages(token, chat.chatId, 20, 0)
        assert(result.isSuccess())
        assert(result.valueOrThrow().isNotEmpty())
    }

    @Test
    fun sendMessage() = runBlocking {
        val chat = createChat()
        assert(MessagesAPI.sendMessage(token, chat.chatId, MessageContent.PlainText("test")).isSuccess())
    }

    @Test
    fun deleteMessage() = runBlocking {
        val chat = createChat()
        val message = MessagesStorage.readAll(chat.chatId, 20, 0).first()
        assert(MessagesAPI.deleteMessage(token, chat.chatId, message.messageId).isSuccess())
    }

}