package com.dove.server.features.chats.messages

import com.dove.data.chats.messages.Message
import com.dove.data.chats.messages.MessageType
import com.dove.server.features.Tags
import com.dove.server.features.chats.members.storage.ChatMembersStorage
import com.dove.server.features.chats.messages.storage.MessagesStorage
import com.dove.server.features.models.ItemsLoadingInfo
import com.dove.server.utils.openapi.get
import com.dove.server.utils.openapi.user
import com.dove.server.utils.openapi.userAuthorized
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag

private val api: MessagesAPI = MessagesAPI(MessagesStorage.Default, ChatMembersStorage.Default)

fun NormalOpenAPIRoute.chatMessages() = tag(Tags.Messages).route("/messages") {
    getMessagesRequest()
    sendMessagesRequest()
    deleteMessagesRequest()
}

private data class GetMessagesRequest(
    @QueryParam("Chat identifier")
    val chatId: Long,
    @QueryParam("Loading information")
    val loadingInfo: ItemsLoadingInfo
)

private fun NormalOpenAPIRoute.getMessagesRequest() = userAuthorized {
    get<GetMessagesRequest, List<Message>> {
        api.getMessages(user, chatId, loadingInfo.number, loadingInfo.offset)
    }
}

private data class SendMessagesRequest(
    @QueryParam("Chat identifier")
    val chatId: Long,
    @QueryParam("Loading information")
    val message: String,
    @QueryParam("Message type")
    val type: MessageType
)

private fun NormalOpenAPIRoute.sendMessagesRequest() = userAuthorized {
    get<SendMessagesRequest, Unit> {
        api.sendMessage(user, chatId, message, type)
    }
}

private data class DeleteMessagesRequest(
    @QueryParam("Chat identifier")
    val chatId: Long,
    @QueryParam("Loading information")
    val messageId: Long
)

private fun NormalOpenAPIRoute.deleteMessagesRequest() = userAuthorized {
    get<DeleteMessagesRequest, Unit> {
        api.deleteMessage(user, chatId, messageId)
    }
}