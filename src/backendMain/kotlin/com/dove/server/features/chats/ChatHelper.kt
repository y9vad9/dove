package com.dove.server.features.chats

import com.dove.data.api.errors.NoSuchPermissionError
import com.dove.data.monad.Either
import com.dove.data.users.User
import com.dove.server.features.chats.members.ChatMembersStorage
import com.dove.server.features.users.UsersStorage

object ChatHelper {
    suspend fun getChatOwner(chatId: Long): User {
        return UsersStorage.read(ChatMembersStorage.readOwner(chatId).memberId)!!
    }

    suspend fun checkIsChatMember(chatId: Long, userId: Long): Either<Unit, Unit> {
        return if (ChatMembersStorage.exists(chatId, userId))
            Either.success(Unit)
        else Either.error(Unit)
    }

    suspend fun checkIsChatOwner(chatId: Long, userId: Long): Either<Unit, NoSuchPermissionError> {
        return if (ChatMembersStorage.readOwner(chatId).memberId != userId)
            Either.error(NoSuchPermissionError("you should be chat owner to perform this action."))
        else Either.success(Unit)
    }
}