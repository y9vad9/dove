package com.dove.backend.server.main

import com.dove.backend.server.main.di.DatabaseDI
import com.dove.backend.storage.core.Storages
import com.dove.backend.storage.core.chats.ChatsStorage
import com.dove.backend.storage.core.chats.members.ChatMembersStorage
import com.dove.backend.storage.core.chats.messages.MessagesStorage
import com.dove.backend.storage.core.files.FilesInfoStorage
import com.dove.backend.storage.core.files.FilesStorage
import com.dove.backend.storage.core.users.UsersStorage
import com.dove.backend.storage.core.users.tokens.TokensStorage
import com.dove.backend.storage.core.users.verifications.VerificationsStorage
import com.dove.backend.storage.database.chats.DatabaseChatsStorage
import com.dove.backend.storage.database.chats.members.DatabaseChatMembersStorage
import com.dove.backend.storage.database.chats.messages.DatabaseMessagesStorage
import com.dove.backend.storage.database.files.DatabaseFilesInfoStorage
import com.dove.backend.storage.database.users.DatabaseUsersStorage
import com.dove.backend.storage.database.users.tokens.DatabaseTokensStorage
import com.dove.backend.storage.database.users.verifications.DatabaseVerificationsStorage
import com.dove.backend.storage.files.DirectoryFilesStorage

object ServerStorages : Storages {
    override val usersStorage: UsersStorage = DatabaseUsersStorage(DatabaseDI.database)
    override val userTokensStorage: TokensStorage = DatabaseTokensStorage(DatabaseDI.database)
    override val verificationsStorage: VerificationsStorage = DatabaseVerificationsStorage(DatabaseDI.database)
    override val filesInfoStorage: FilesInfoStorage = DatabaseFilesInfoStorage(DatabaseDI.database)
    override val filesStorage: FilesStorage = DirectoryFilesStorage(Environment.files)
    override val chatMembersStorage: ChatMembersStorage = DatabaseChatMembersStorage(DatabaseDI.database)
    override val chatsStorage: ChatsStorage =
        DatabaseChatsStorage(DatabaseDI.database, chatMembersStorage, usersStorage)
    override val chatMessagesStorage: MessagesStorage =
        DatabaseMessagesStorage(DatabaseDI.database, usersStorage, filesInfoStorage)
}