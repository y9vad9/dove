package com.dove.backend.storage.database.chats.members

import com.dove.backend.storage.core.chats.members.ChatMembersStorage
import com.dove.data.chats.Member
import com.dove.data.chats.MemberType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseChatMembersStorage(database: Database) : ChatMembersStorage {
    private object ChatMembers : Table() {
        /**
         * Identifier of the chat where member exists.
         */
        val CHAT_ID: Column<Long> = long("chat_id")

        /**
         * Identifier of member that exists in chat.
         */
        val MEMBER_ID: Column<Long> = long("member_id")

        val MEMBER_TYPE: Column<MemberType> = enumeration("member_type", MemberType::class)

        /**
         * Time in milliseconds when user joined.
         * @see [timeInMs].
         */
        val MEMBER_JOINED_TIME: Column<Long> = long("member_joined_time")
    }

    init {
        transaction(database) {
            SchemaUtils.create(ChatMembers)
        }
    }

    /**
     * Adds new member with [memberId] to chat with [chatId].
     * @see com.dove.backend.storage.database.chats.DatabaseChatsStorage.Chats.CHAT_ID
     */
    override suspend fun create(chatId: Long, memberId: Long, memberType: MemberType, time: Long): Unit =
        newSuspendedTransaction {
            ChatMembers.insert {
                it[CHAT_ID] = chatId
                it[MEMBER_ID] = memberId
                it[MEMBER_JOINED_TIME] = time
            }
        }

    /**
     * Deletes member from chat.
     * @param chatId - number identifier of the chat.
     * @param memberId - number identifier of the user.
     */
    override suspend fun delete(chatId: Long, memberId: Long): Unit = newSuspendedTransaction {
        ChatMembers.deleteWhere { (ChatMembers.CHAT_ID eq chatId) and (ChatMembers.MEMBER_ID eq memberId) }
    }

    /**
     * Gets members of chat with [chatId].
     * @param chatId - identifier of chat.
     * @param number - number of columns that will be loaded.
     * @param offset - position from which it will load.
     */
    override suspend fun readAll(chatId: Long, number: Int, offset: Long): List<Member> = newSuspendedTransaction {
        ChatMembers.select { ChatMembers.CHAT_ID eq chatId }
            .limit(number, offset)
            .toList()
            .map { it.toMember() }
    }

    /**
     * Gets chat owner by [chatId].
     */
    override suspend fun readOwner(chatId: Long): Member = newSuspendedTransaction {
        ChatMembers.select {
            (ChatMembers.CHAT_ID eq chatId) and (ChatMembers.MEMBER_TYPE eq MemberType.OWNER)
        }.single().toMember()
    }

    override suspend fun exists(chatId: Long, userId: Long) = newSuspendedTransaction {
        ChatMembers.select { (ChatMembers.CHAT_ID eq chatId) and (ChatMembers.MEMBER_ID eq userId) }
            .firstOrNull() != null
    }

    /**
     * Gets chat ids where user with id [memberId] exists.
     * @param memberId - user identifier.
     * @param number - number of columns that will be loaded.
     * @param offset - position from which it will load.
     */
    override suspend fun chatIdsUserExistIn(memberId: Long, number: Int, offset: Long): List<Long> =
        newSuspendedTransaction {
            ChatMembers.select { ChatMembers.MEMBER_ID eq memberId }
                .limit(number, offset)
                .toList()
                .map { it.toMember().memberId }
        }

    override suspend fun deleteAll(): Unit = newSuspendedTransaction {
        ChatMembers.deleteAll()
    }

    private fun ResultRow.toMember(): Member = Member(get(ChatMembers.MEMBER_ID), get(ChatMembers.MEMBER_TYPE))
}