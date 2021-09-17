package com.dove.server.features.chats

import com.dove.data.api.errors.NoSuchPermissionError
import com.dove.data.monad.Either
import com.dove.server.features.chats.members.storage.ChatMembersStorage

object ChatHelper {

    suspend fun checkIsChatMember(storage: ChatMembersStorage, chatId: Long, userId: Long): Either<Unit, Unit> {
        return if (storage.exists(chatId, userId))
            Either.success(Unit)
        else Either.error(Unit)
    }

    suspend fun checkIsChatOwner(
        storage: ChatMembersStorage,
        chatId: Long,
        userId: Long
    ): Either<Unit, NoSuchPermissionError> {
        return if (storage.readOwner(chatId).memberId != userId)
            Either.error(NoSuchPermissionError("you should be chat owner to perform this action."))
        else Either.success(Unit)
    }
}