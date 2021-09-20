package com.dove.server.features.chats

import com.dove.data.chats.Chat
import com.dove.server.features.Tags
import com.dove.server.features.chats.members.chatMembers
import com.dove.server.features.chats.members.storage.ChatMembersStorage
import com.dove.server.features.chats.messages.chatMessages
import com.dove.server.features.chats.storage.ChatsStorage
import com.dove.server.features.models.ItemsLoadingInfo
import com.dove.server.utils.openapi.*
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag

private val api: ChatsAPI = ChatsAPI(ChatsStorage.Default, ChatMembersStorage.Default)

fun NormalOpenAPIRoute.chats() = tag(Tags.Chats).route("/chats") {
    getUsersChatsRequest()
    createGroupRequest()
    createPersonalRequest()
    editGroupRequest()
    deleteChatRequest()

    chatMembers()
    chatMessages()
}

private fun NormalOpenAPIRoute.getUsersChatsRequest() = userAuthorized {
    get<ItemsLoadingInfo, List<Chat>> {
        api.getUserChats(user, number, offset)
    }
}

private data class CreateChatRequest(
    @QueryParam("Chat name")
    val name: String
)

private fun NormalOpenAPIRoute.createGroupRequest() = userAuthorized {
    post<CreateChatRequest, Chat.Group, Unit> {
        api.createGroup(user, name)
    }
}

private data class CreatePersonalRequest(
    @QueryParam("User to chat")
    val userId: Long
)

private fun NormalOpenAPIRoute.createPersonalRequest() = userAuthorized {
    post<CreatePersonalRequest, Chat.Personal, Unit> {
        api.createPersonalChat(user, userId)
    }
}

private data class EditGroupRequest(
    @QueryParam("Group ID")
    val groupId: Long,
    @QueryParam("New name")
    val name: String?,
    @QueryParam("New chat image")
    val image: String?
)

private fun NormalOpenAPIRoute.editGroupRequest() = userAuthorized {
    patch<EditGroupRequest, Unit, Unit> {
        api.updateGroupInfo(user, groupId, name, image)
    }
}

private data class DeleteGroupRequest(
    @QueryParam("Group ID")
    val groupId: Long
)

private fun NormalOpenAPIRoute.deleteChatRequest() = userAuthorized {
    patch<DeleteGroupRequest, Unit, Unit> {
        api.deleteChat(user, groupId)
    }
}