package com.dove.backend.server.api.routings

import com.dove.backend.apis.chats.ChatsAPI
import com.dove.backend.apis.chats.members.ChatMembersAPI
import com.dove.backend.apis.chats.messages.MessagesAPI
import com.dove.backend.apis.files.FilesAPI
import com.dove.backend.apis.users.UsersAPI
import com.dove.backend.apis.users.tokens.TokensAPI
import com.dove.backend.apis.users.verifications.VerificationsAPI
import com.dove.backend.server.api.routings.chats.chats
import com.dove.backend.server.api.routings.files.files
import com.dove.backend.server.api.routings.users.users
import com.dove.backend.storage.core.Storages
import com.dove.data.users.User
import com.dove.mailer.Mailer
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.y9neon.middleware.authorization.Authorization

fun NormalOpenAPIRoute.rootRouting(storages: Storages, mailer: Mailer, authorization: Authorization.Feature<User>) =
    with(storages) {
        users(
            api = UsersAPI(usersStorage),
            tokensAPI = TokensAPI(userTokensStorage, usersStorage, verificationsStorage, mailer),
            verificationsAPI = VerificationsAPI(verificationsStorage, usersStorage, userTokensStorage),
            authorization
        )
        files(
            api = FilesAPI(filesInfoStorage, filesStorage),
            authorization
        )
        chats(
            api = ChatsAPI(chatsStorage, chatMembersStorage),
            chatMembersAPI = ChatMembersAPI(chatMembersStorage, usersStorage),
            messagesAPI = MessagesAPI(chatMessagesStorage, chatMembersStorage),
            authorization
        )
    }