package com.dove.server.features.chats.members.storage

import com.dove.data.chats.Member
import com.dove.data.chats.MemberType

class MockedChatMembersStorage : ChatMembersStorage {
    private val chatMembers: MutableMap<Long, List<Member>> = mutableMapOf()

    override suspend fun create(chatId: Long, memberId: Long, memberType: MemberType) {
        chatMembers[chatId] = (chatMembers[chatId] ?: listOf()) + Member(memberId, memberType)
    }

    override suspend fun delete(chatId: Long, memberId: Long) {
        chatMembers[chatId] = (chatMembers[chatId] ?: listOf()).filter { it.memberId == memberId }
    }

    override suspend fun readAll(chatId: Long, number: Int, offset: Long): List<Member> {
        return chatMembers[chatId]?.subList(offset.toInt(), offset.toInt() + number) ?: error("No such chat.")
    }

    override suspend fun readOwner(chatId: Long): Member {
        return chatMembers[chatId]?.firstOrNull { it.memberType == MemberType.OWNER }
            ?: error("No one owner registered for $chatId.")
    }

    override suspend fun exists(chatId: Long, userId: Long): Boolean {
        return chatMembers[chatId]?.firstOrNull { it.memberId == userId } != null
    }

    override suspend fun chatIdsUserExistIn(memberId: Long, number: Int, offset: Long): List<Long> {
        return chatMembers
            .filter { memberId in it.value.map(Member::memberId) }
            .flatMap { it.value }
            .map { it.memberId }
    }

    override suspend fun deleteAll() {
        chatMembers.clear()
    }
}