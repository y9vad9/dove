package com.dove.server.features.chats

import com.dove.data.chats.ChatType
import com.dove.data.chats.MemberType
import com.dove.data.monad.isSuccess
import com.dove.server.features.chats.members.storage.ChatMembersStorage
import com.dove.server.features.chats.members.storage.MockedChatMembersStorage
import com.dove.server.features.chats.storage.ChatsStorage
import com.dove.server.features.chats.storage.MockedChatsStorage
import com.dove.server.features.users.storage.MockedUsersStorage
import com.dove.server.features.users.storage.UsersStorage
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
        val chatId = chatsStorage.create("test", null, ChatType.GROUP)
        println(chatId)
        chatMembersStorage.create(chatId, user.id, MemberType.OWNER)
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
        val group = chatsStorage.create("test", null, ChatType.GROUP)
        chatMembersStorage.create(group, user.id, MemberType.OWNER)
        assert(api.updateGroupInfo(user, group, "test", null).isSuccess())
    }

    @Test
    fun deleteChat(): Unit = runBlocking {
        val group = chatsStorage.create("test", null, ChatType.GROUP)
        chatMembersStorage.create(group, user.id, MemberType.OWNER)
        assert(api.deleteChat(user, group).isSuccess())
    }
}