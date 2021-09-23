package com.dove.backend.storage.core

import com.dove.backend.storage.core.chats.ChatsStorage
import com.dove.backend.storage.core.chats.members.ChatMembersStorage
import com.dove.backend.storage.core.chats.messages.MessagesStorage
import com.dove.backend.storage.core.files.FilesInfoStorage
import com.dove.backend.storage.core.files.FilesStorage
import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.core.users.tokens.TokensStorage
import com.dove.backend.storage.core.users.verifications.VerificationsStorage

/**
 * All storages of dove.
 */
interface Storages {
    val usersStorage: UsersStorage
    val userTokensStorage: TokensStorage
    val verificationsStorage: VerificationsStorage
    val filesInfoStorage: FilesInfoStorage
    val filesStorage: FilesStorage
    val chatsStorage: ChatsStorage
    val chatMessagesStorage: MessagesStorage
    val chatMembersStorage: ChatMembersStorage
}