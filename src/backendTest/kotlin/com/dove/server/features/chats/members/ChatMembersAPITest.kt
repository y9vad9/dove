package com.dove.server.features.chats.members

import com.dove.data.Constants
import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import com.dove.data.chats.MemberType
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrThrow
import com.dove.data.users.User
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.chats.ChatsStorage
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
object ChatMembersAPITest {

    private val userId get() = user.id
    private val token by lazy { Random.nextString(Constants.TOKEN_LENGTH) }
    private const val chatName: String = "Chat"
    private var user by Delegates.notNull<User>()

    @BeforeEach
    fun deleteItems() = runBlocking {
        TokensStorage.deleteAll()
        ChatMembersStorage.deleteAll()
    }

    @BeforeAll
    fun createUser() = runBlocking {
        user = UsersStorage.create("test@email.com")
    }

    private fun createChat(): Chat = runBlocking {
        val auth = TokensStorage.create(userId, token, timeInMs, TokenType.REGULAR)
        val chatId = ChatsStorage.create(chatName, null, ChatType.GROUP)
        ChatMembersStorage.create(chatId, auth.userId, MemberType.OWNER)
        ChatMembersStorage.create(chatId, Random.nextLong(Long.MAX_VALUE), MemberType.REGULAR)
        return@runBlocking ChatsStorage.read(chatId, userId)!!
    }

    @Test
    fun getMembers() = runBlocking {
        val chat = createChat()
        val membersResult = ChatMembersAPI.getMembers(user, chat.chatId, 20, 0)
        assert(membersResult.isSuccess())
        assert(membersResult.valueOrThrow().isNotEmpty())
    }

    @Test
    fun addMember() = runBlocking {
        val chat = createChat()
        assert(ChatMembersAPI.addMember(user, chat.chatId, 2).isSuccess())
    }

    @Test
    fun kickMember() = runBlocking {
        val chat = createChat()
        ChatMembersAPI.addMember(user, chat.chatId, 2)
        assert(ChatMembersAPI.kickMember(user, chat.chatId, 2).isSuccess())
    }

}