package com.dove.server.features.chats

import com.dove.data.Constants
import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import com.dove.data.chats.GroupInfo
import com.dove.data.chats.MemberType
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrNull
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.chats.members.ChatMembersStorage
import com.dove.server.features.users.UsersStorage
import com.dove.server.features.users.tokens.TokensStorage
import com.dove.server.utils.random.nextString
import com.dove.server.utils.time.timeInMs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import kotlin.random.Random

@Testable
object ChatsAPITest {
    @BeforeEach
    fun deleteItems() = runBlocking {
        ChatsStorage.deleteAll()
        ChatMembersStorage.deleteAll()
    }

    @Test
    fun createGroup() = runBlocking {
        val token = TokensStorage.create(1, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        assert(ChatsAPI.createGroup(token.token, "Test").isSuccess())
    }

    @Test
    fun createPersonalChat() = runBlocking {
        val userId = UsersStorage.create("test").id
        val userIdAnother = UsersStorage.create("test2").id
        val token = TokensStorage.create(userId, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        assert(ChatsAPI.createPersonalChat(token.token, userIdAnother).isSuccess())
    }


    @Test
    fun getUserChats() = runBlocking {
        ChatMembersStorage.create(1, 1, MemberType.REGULAR)
        val token = TokensStorage.create(1, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        assert(ChatsAPI.getUserChats(token.token, 20, 0).valueOrNull()?.isNotEmpty() == true)
    }

    @Test
    fun deleteChat() = runBlocking {
        val userId = UsersStorage.create("test").id
        val chatId = ChatsStorage.create("chat", null, ChatType.GROUP)
        val token = TokensStorage.create(userId, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        ChatMembersStorage.create(chatId, token.userId, MemberType.OWNER)
        assert(ChatsAPI.deleteChat(token.token, chatId).isSuccess())
        assert(ChatsStorage.read(chatId, userId) == null)
    }

    @Test
    fun updateInfo() = runBlocking {
        val userId = UsersStorage.create("test").id
        val chatId = ChatsStorage.create("chat", "", ChatType.GROUP)
        val token = TokensStorage.create(userId, Random.nextString(Constants.TOKEN_LENGTH), timeInMs, TokenType.REGULAR)
        ChatMembersStorage.create(chatId, userId, MemberType.OWNER)
        assert(ChatsAPI.updateGroupInfo(token.token, chatId, GroupInfo("new name", null)).isSuccess())
        assert((ChatsStorage.read(chatId, userId) as? Chat.Group)?.let { it.name == "new name" && it.image == null }
            ?: false)
    }


}