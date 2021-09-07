package com.dove.server.features.chats

import com.dove.data.chats.Chat
import com.dove.data.chats.GroupInfo
import com.dove.server.features.chats.members.chatMembers
import com.dove.server.features.chats.messages.chatMessages
import com.dove.server.features.models.ItemsLoadingInfo
import com.dove.server.utils.openapi.*
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route

fun NormalOpenAPIRoute.chats() = route("/chats") {
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
        ChatsAPI.getUserChats(user, number, offset)
    }
}

private data class CreateChatRequest(
    @QueryParam("Chat name")
    val name: String
)

private fun NormalOpenAPIRoute.createGroupRequest() = userAuthorized {
    post<CreateChatRequest, Chat.Group, Unit> {
        ChatsAPI.createGroup(user, name)
    }
}

private data class CreatePersonalRequest(
    @QueryParam("User to chat")
    val userId: Long
)

private fun NormalOpenAPIRoute.createPersonalRequest() = userAuthorized {
    post<CreatePersonalRequest, Chat.Personal, Unit> {
        ChatsAPI.createPersonalChat(user, userId)
    }
}

private data class EditGroupRequest(
    @QueryParam("Group ID")
    val groupId: Long,
    @QueryParam("New Group information")
    val info: GroupInfo
)

private fun NormalOpenAPIRoute.editGroupRequest() = userAuthorized {
    patch<EditGroupRequest, Unit, Unit> {
        ChatsAPI.updateGroupInfo(user, groupId, info)
    }
}

private data class DeleteGroupRequest(
    @QueryParam("Group ID")
    val groupId: Long
)

private fun NormalOpenAPIRoute.deleteChatRequest() = userAuthorized {
    patch<DeleteGroupRequest, Unit, Unit> {
        ChatsAPI.deleteChat(user, groupId)
    }
}