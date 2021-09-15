package com.dove.server.features.chats.members

import com.dove.data.chats.ChatType
import com.dove.data.chats.MemberType
import com.dove.data.monad.isSuccess
import com.dove.data.monad.valueOrThrow
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
object ChatMembersAPITest {
    private val usersStorage: UsersStorage = MockedUsersStorage()
    private val chatMembersStorage: ChatMembersStorage = MockedChatMembersStorage()
    private val chatsStorage: ChatsStorage = MockedChatsStorage(chatMembersStorage, usersStorage)

    private val api: ChatMembersAPI = ChatMembersAPI(chatMembersStorage, usersStorage)

    private val user by lazy { runBlocking { usersStorage.create("test@testing.email") } }

    @Test
    fun getMembers(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", null, ChatType.GROUP)
        chatMembersStorage.create(chatId, user.id, MemberType.REGULAR)
        repeat(5) {
            val user2 = usersStorage.create("test$it@email.net")
            chatMembersStorage.create(chatId, user2.id, MemberType.REGULAR)
        }
        api.getMembers(user, chatId, 20, 0).also {
            assert(it.isSuccess())
        }.also {
            assert(it.valueOrThrow().isNotEmpty())
        }
    }

    @Test
    fun addMember(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", null, ChatType.GROUP)
        chatMembersStorage.create(chatId, user.id, MemberType.OWNER)
        val another = usersStorage.create("another@testing.email")
        assert(api.addMember(user, chatId, another.id).isSuccess())
    }

    @Test
    fun kickMember(): Unit = runBlocking {
        val chatId = chatsStorage.create("test", null, ChatType.GROUP)
        chatMembersStorage.create(chatId, user.id, MemberType.OWNER)
        val another = usersStorage.create("another@testing.email")
        chatMembersStorage.create(chatId, another.id, MemberType.REGULAR)
        assert(api.kickMember(user, chatId, another.id).isSuccess())
    }
}