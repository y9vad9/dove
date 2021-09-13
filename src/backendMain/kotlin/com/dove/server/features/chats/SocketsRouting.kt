package com.dove.server.features.chats

import com.dove.data.api.events.ChatEvent
import com.dove.data.chats.updates.SubscribeToChats
import com.dove.data.chats.updates.UnsubscribeFromChats
import com.dove.data.monad.isSuccess
import com.dove.server.features.users.tokens.TokensStorage
import com.dove.server.utils.updater.Updater
import com.y9neon.ktor.jsonrpc.JsonRpcRequestsBuilder
import com.y9neon.ktor.jsonrpc.JsonRpcSession
import com.y9neon.ktor.jsonrpc.request
import com.y9neon.ktor.jsonrpc.webSocket
import io.ktor.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import java.util.*

private data class Listener(
    val userId: Long,
    val chats: MutableList<Long>,
    val scope: CoroutineScope,
    var session: JsonRpcSession
) {
    fun startListening() = scope.launch {
        Updater.events.filterIsInstance<ChatEvent>()
            .filter { it.user.id == userId }
            .filter { it.chat.chatId in chats }
            .collect { session.send(it) }
    }

    fun stopListening() = scope.cancel()
}

private val listeners = Collections.synchronizedList(mutableListOf<Listener>())

fun Routing.chatsSocket() {
    webSocket("/chat/updates") {
        subscribeChats()
        unsubscribeChats()
    }
}

private fun JsonRpcRequestsBuilder.unsubscribeChats() {
    request<UnsubscribeFromChats>("unsubscribe") { parameters ->
        val auth = TokensStorage.read(parameters.token) ?: return@request
        for (chatId in parameters.ids) {
            listeners.firstOrNull { it.userId == auth.userId }?.chats?.removeAll(parameters.ids)
        }
    }
}

private fun JsonRpcRequestsBuilder.subscribeChats() {
    request<SubscribeToChats>("subscribe") { parameters ->
        val auth = TokensStorage.read(parameters.token) ?: return@request
        for (chatId in parameters.ids) {
            if (ChatHelper.checkIsChatMember(chatId, auth.userId).isSuccess()) {
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