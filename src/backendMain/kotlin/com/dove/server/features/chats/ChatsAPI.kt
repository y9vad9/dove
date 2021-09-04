package com.dove.server.features.chats

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.ChatNotFoundError
import com.dove.data.api.errors.InvalidTokenError
import com.dove.data.chats.Chat
import com.dove.data.chats.ChatType
import com.dove.data.chats.GroupInfo
import com.dove.data.monad.Either
import com.dove.data.monad.isSuccess
import com.dove.data.monad.onError
import com.dove.data.monad.valueOrThrow
import com.dove.data.users.tokens.TokenType
import com.dove.server.features.chats.members.ChatMembersStorage
import com.dove.server.features.users.tokens.TokensHelper
import com.dove.server.features.users.tokens.TokensStorage

object ChatsAPI {
    /**
     * Gets [number] user chats with [offset].
     */
    suspend fun getUserChats(token: String, number: Int, offset: Long): ApiResult<List<Chat>> {
        val auth = TokensStorage.read(token) ?: return Either.error(InvalidTokenError())
        if (auth.type != TokenType.REGULAR)
            return Either.error(InvalidTokenError())
        return Either.success(ChatsStorage.readAll(auth.userId, number, offset))
    }

    suspend fun create(token: String, name: String): ApiResult<Chat.Group> {
        val auth = TokensStorage.read(token) ?: return Either.error(InvalidTokenError())
        if (auth.type != TokenType.REGULAR)
            return Either.error(InvalidTokenError())
        val chatId = ChatsStorage.create(name, null, ChatType.GROUP)
        ChatMembersStorage.create(chatId, auth.userId)
        return Either.success(ChatsStorage.read(chatId, auth.userId) as Chat.Group)
    }

    suspend fun updateGroupInfo(token: String, chatId: Long, groupInfo: GroupInfo): ApiResult<Unit> {
        val auth = TokensHelper.checkRegularAuthorization(token).onError {
            return Either.error(it)
        }.valueOrThrow()

        val group = ChatsStorage.read(chatId, auth.userId)
            ?.takeIf { it.type == ChatType.GROUP }
            ?: return Either.error(ChatNotFoundError)

        ChatHelper.checkIsChatOwner(chatId, auth.userId).onError {
            return Either.error(it)
        }

        return Either.success(ChatsStorage.update(chatId, groupInfo.name, groupInfo.image))
    }

    suspend fun deleteChat(token: String, chatId: Long): ApiResult<Unit> {
        val auth = TokensHelper.checkRegularAuthorization(token).onError {
            return Either.error(it)
        }.valueOrThrow()

        val check = ChatHelper.checkIsChatOwner(chatId, auth.userId)

        return if (check.isSuccess())
            Either.success(ChatsStorage.delete(chatId))
        else
            Either.error(check.error)
    }

}