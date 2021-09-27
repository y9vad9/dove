package com.dove.backend.storage.core.chats.members

import com.dove.data.chats.Member
import com.dove.data.chats.MemberType
import org.jetbrains.annotations.TestOnly

interface ChatMembersStorage {
    /**
     * Adds new member with [memberId] to chat with [chatId].
     * @see com.dove.server.features.chats.DatabaseChatsStorage.Chats.CHAT_ID
     */
    suspend fun create(chatId: Long, memberId: Long, memberType: MemberType, time: Long)

    /**
     * Deletes member from chat.
     * @param chatId - number identifier of the chat.
     * @param memberId - number identifier of the user.
     */
    suspend fun delete(chatId: Long, memberId: Long)

    /**
     * Gets members of chat with [chatId].
     * @param chatId - identifier of chat.
     * @param number - number of columns that will be loaded.
     * @param offset - position from which it will load.
     */
    suspend fun readAll(chatId: Long, number: Int, offset: Long): List<Member>

    /**
     * Gets chat owner by [chatId].
     */
    suspend fun readOwner(chatId: Long): Member

    /**
     * Checks is user exist in chat.
     * @param chatId - unique chat identifier.
     * @param userId - user id that should be checked for existence.
     */
    suspend fun exists(chatId: Long, userId: Long): Boolean

    /**
     * Gets chat ids where user with id [memberId] exists.
     * @param memberId - user identifier.
     * @param number - number of columns that will be loaded.
     * @param offset - position from which it will load.
     */
    suspend fun chatIdsUserExistIn(memberId: Long, number: Int, offset: Long): List<Long>

    /**
     * Deletes everything in storage.
     */
    @TestOnly
    suspend fun deleteAll()
}