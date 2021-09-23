package com.dove.backend.apis.chats

import com.dove.backend.features.time.timeInMs
import com.dove.backend.storage.core.chats.ChatsStorage
import com.dove.backend.storage.core.chats.members.ChatMembersStorage
import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.mocked.chats.MockedChatsStorage
import com.dove.backend.storage.mocked.chats.members.MockedChatMembersStorage
import com.dove.backend.storage.mocked.users.MockedUsersStorage
import com.dove.data.chats.ChatType
import com.dove.data.chats.MemberType
import com.dove.data.monad.isSuccess
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable

@Testable
object ChatsAPITest {
    private val usersStorage: UsersStorage = MockedUsersStorage()
    private val chatMembersStorage: ChatMembersStorage = MockedChatMembersStorage()
    private val chatsStorage: ChatsStorage = MockedChatsStorage(chatMembersStorage, usersStorage)

    private val api: ChatsAPI = ChatsAPI(chatsStorage, chatMembersStorage)

    private val user by lazy { runBlocking { usersStorage.create("test@testing.email") } }

    @Test
    fun getUserChats() = runBlocking {
        val chatId = chatsStorage.create("test", null, ChatType.GROUP, timeInMs)
        println(chatId)
        chatMembersStorage.create(chatId, user.id, MemberType.OWNER, timeInMs)
        assert(api.getUserChats(user, 10, 0).isSuccess())
    }

    @Test
    fun createGroup() = runBlocking {
        assert(api.createGroup(user, "test").isSuccess())
    }

    @Test
    fun createPersonalChat() = runBlocking {
        val companion = usersStorage.create("another@testing.email")
        assert(api.createPersonalChat(user, companion.id).isSuccess())
    }

    @Test
    fun updateGroupInfo(): Unit = runBlocking {
        val group = chatsStorage.create("test", null, ChatType.GROUP, timeInMs)
        chatMembersStorage.create(group, user.id, MemberType.OWNER, timeInMs)
        assert(api.updateGroupInfo(user, group, "test", null).isSuccess())
    }

    @Test
    fun deleteChat(): Unit = runBlocking {
        val group = chatsStorage.create("test", null, ChatType.GROUP, timeInMs)
        chatMembersStorage.create(group, user.id, MemberType.OWNER, timeInMs)
        assert(api.deleteChat(user, group).isSuccess())
    }
}