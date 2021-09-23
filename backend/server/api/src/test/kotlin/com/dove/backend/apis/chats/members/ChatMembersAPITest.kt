package com.dove.backend.apis.chats.members

import com.dove.backend.storage.core.chats.ChatsStorage
import com.dove.backend.storage.core.chats.members.ChatMembersStorage
import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.mocked.chats.MockedChatsStorage
import com.dove.backend.storage.mocked.chats.members.MockedChatMembersStorage
import com.dove.backend.storage.mocked.users.MockedUsersStorage
import com.dove.data.chats.ChatType
import com.dove.data.chats.MemberType
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrThrow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable

@Testable
object ChatMembersAPITest {
    private val usersStorage: UsersStorage = MockedUsersStorage()
    private val chatMembersStorage: ChatMembersStorage = MockedChatMembersStorage()
    private val chatsStorage: ChatsStorage = MockedChatsStorage(chatMembersStorage, usersStorage)

    private val api: ChatMembersAPI = ChatMembersAPI(chatMembersStorage, usersStorage)

    private val user by lazy { runBlocking { usersStorage.create("test@testing.email") } }

    @Test
    fun getMembers(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", null, ChatType.GROUP, 0)
        chatMembersStorage.create(chatId, user.id, MemberType.REGULAR, 0)
        repeat(5) {
            val user2 = usersStorage.create("test$it@email.net")
            chatMembersStorage.create(chatId, user2.id, MemberType.REGULAR, 0)
        }
        api.getMembers(user, chatId, 20, 0).also {
            assert(it.isSuccess())
        }.also {
            assert(it.valueOrThrow().isNotEmpty())
        }
    }

    @Test
    fun addMember(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", null, ChatType.GROUP, 0)
        chatMembersStorage.create(chatId, user.id, MemberType.OWNER, 0)
        val another = usersStorage.create("another@testing.email")
        assert(api.addMember(user, chatId, another.id).isSuccess())
    }

    @Test
    fun kickMember(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", null, ChatType.GROUP, 0)
        chatMembersStorage.create(chatId, user.id, MemberType.OWNER, 0)
        val another = usersStorage.create("another@testing.email")
        chatMembersStorage.create(chatId, another.id, MemberType.REGULAR, 0)
        assert(api.kickMember(user, chatId, another.id).isSuccess())
    }
}