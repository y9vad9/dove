package com.dove.server.features.chats

import com.dove.data.Constants
import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import com.dove.data.chats.GroupInfo
import com.dove.data.chats.MemberType
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrNull
import com.dove.data.users.User
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.chats.members.ChatMembersStorage
import com.dove.server.features.users.UsersStorage
import com.dove.server.features.users.tokens.TokensStorage
import com.dove.server.utils.random.nextString
import com.dove.server.utils.time.timeInMs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.platform.commons.annotation.Testable
import kotlin.properties.Delegates
import kotlin.random.Random

@Testable
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
object ChatsAPITest {

    private val userId get() = user.id
    private val token by lazy { Random.nextString(Constants.TOKEN_LENGTH) }
    private const val chatName: String = "Chat"
    private var user by Delegates.notNull<User>()

    @BeforeEach
    fun deleteItems() = runBlocking {
        ChatsStorage.deleteAll()
        ChatMembersStorage.deleteAll()
        TokensStorage.deleteAll()
        UsersStorage.deleteAll()
    }

    @BeforeAll
    @JvmStatic
    fun createUser() = runBlocking {
        user = UsersStorage.create("test@email.com")
    }

    @Test
    fun createGroup() = runBlocking {
        val token = TokensStorage.create(1, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        assert(ChatsAPI.createGroup(user, "Test").isSuccess())
    }

    @Test
    fun createPersonalChat() = runBlocking {
        val userId = UsersStorage.create("test").id
        val userIdAnother = UsersStorage.create("test2").id
        val token = TokensStorage.create(userId, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        assert(ChatsAPI.createPersonalChat(user, userIdAnother).isSuccess())
    }


    @Test
    fun getUserChats() = runBlocking {
        ChatMembersStorage.create(1, 1, MemberType.REGULAR)
        val token = TokensStorage.create(1, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        assert(ChatsAPI.getUserChats(user, 20, 0).valueOrNull()?.isNotEmpty() == true)
    }

    @Test
    fun deleteChat() = runBlocking {
        val userId = UsersStorage.create("test").id
        val chatId = ChatsStorage.create("chat", null, ChatType.GROUP)
        val token = TokensStorage.create(userId, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        ChatMembersStorage.create(chatId, token.userId, MemberType.OWNER)
        assert(ChatsAPI.deleteChat(user, chatId).isSuccess())
        assert(ChatsStorage.read(chatId, userId) == null)
    }

    @Test
    fun updateInfo() = runBlocking {
        val userId = UsersStorage.create("test").id
        val chatId = ChatsStorage.create("chat", "", ChatType.GROUP)
        val token = TokensStorage.create(userId, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        ChatMembersStorage.create(chatId, userId, MemberType.OWNER)
        assert(ChatsAPI.updateGroupInfo(user, chatId, GroupInfo("new name", null)).isSuccess())
        assert(
            (ChatsStorage.read(chatId, userId) as? Chat.Group)
                ?.let { it.name == "new name" && it.image == null }
                ?: false
        )
    }


}