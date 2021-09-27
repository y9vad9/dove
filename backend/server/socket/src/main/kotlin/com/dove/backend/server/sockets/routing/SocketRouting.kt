package com.dove.backend.server.sockets.routing

import com.dove.backend.apis.chats.ChatHelper
import com.dove.backend.server.events.EventsDispatcher
import com.dove.backend.storage.core.chats.members.ChatMembersStorage
import com.dove.backend.storage.core.users.tokens.TokensStorage
import com.dove.data.api.events.ChatEvent
import com.dove.data.chats.updates.SubscribeToChats
import com.dove.data.chats.updates.UnsubscribeFromChats
import com.dove.data.monad.isSuccess
import com.y9neon.ktor.jsonrpc.JsonRpcRequestsBuilder
import com.y9neon.ktor.jsonrpc.JsonRpcSession
import com.y9neon.ktor.jsonrpc.request
import com.y9neon.ktor.jsonrpc.webSocket
import io.ktor.routing.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import java.util.*

class SocketRouting(private val tokensStorage: TokensStorage, private val chatMembersStorage: ChatMembersStorage) {

    private data class Listener(
        val userId: Long,
        val chats: MutableList<Long>,
        val scope: CoroutineScope,
        var session: JsonRpcSession
    ) {
        @OptIn(InternalCoroutinesApi::class)
        fun startListening() = scope.launch {
            EventsDispatcher.filterIsInstance<ChatEvent>()
                .filter { it.user.id == userId }
                .filter { it.chat.chatId in chats }
                .collect { session.send(it) }
        }

        fun stopListening() = scope.cancel()
    }

    private val listeners = Collections.synchronizedList(mutableListOf<Listener>())

    fun chatsSocket(routing: Routing) = with(routing) {
        webSocket("/chat/updates") {
            subscribeChats()
            unsubscribeChats()
        }
    }

    private fun JsonRpcRequestsBuilder.unsubscribeChats() {
        request<UnsubscribeFromChats>("unsubscribe") { parameters ->
            val auth = tokensStorage.read(parameters.token) ?: return@request
            for (chatId in parameters.ids) {
                listeners.firstOrNull { it.userId == auth.userId }?.chats?.removeAll(parameters.ids)
            }
        }
    }

    private fun JsonRpcRequestsBuilder.subscribeChats() {
        request<SubscribeToChats>("subscribe") { parameters ->
            val auth = tokensStorage.read(parameters.token) ?: return@request
            for (chatId in parameters.ids) {
                if (ChatHelper.checkIsChatMember(chatMembersStorage, chatId, auth.userId).isSuccess()) {
                    if (!listeners.any { it.userId == auth.userId })
                        listeners.add(
                            Listener(
                                auth.userId,
                                parameters.ids.toMutableList(),
                                CoroutineScope(Dispatchers.IO),
                                this
                            )
                        )
                    else listeners.first { it.userId == auth.userId }.apply {
                        session = this@request
                        chats += parameters.ids
                    }
                }
            }
        }
    }
}