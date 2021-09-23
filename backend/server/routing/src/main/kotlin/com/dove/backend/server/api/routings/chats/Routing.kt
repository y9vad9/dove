package com.dove.backend.server.api.routings.chats

import com.dove.backend.apis.chats.ChatsAPI
import com.dove.backend.apis.chats.members.ChatMembersAPI
import com.dove.backend.apis.chats.messages.MessagesAPI
import com.dove.backend.server.api.routings.Tags
import com.dove.backend.server.api.routings.chats.members.chatMembers
import com.dove.backend.server.api.routings.chats.messages.chatMessages
import com.dove.backend.server.api.routings.users.UsersAuthorizedScope
import com.dove.backend.server.models.ItemsLoadingInfo
import com.dove.data.chats.Chat
import com.dove.data.users.User
import com.papsign.ktor.openapigen.annotations.Path
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import com.y9neon.middleware.authorization.Authorization
import com.y9neon.openapi.get
import com.y9neon.openapi.patch
import com.y9neon.openapi.post

fun NormalOpenAPIRoute.chats(
    api: ChatsAPI,
    chatMembersAPI: ChatMembersAPI,
    messagesAPI: MessagesAPI,
    feature: Authorization.Feature<User>
) =
    tag(Tags.Chats).route("/chats") {

        fun userAuthorized(block: UsersAuthorizedScope<NormalOpenAPIRoute>.() -> Unit) {
            block(UsersAuthorizedScope(this, feature))
        }

        fun NormalOpenAPIRoute.getUsersChatsRequest() = userAuthorized {
            get<ItemsLoadingInfo, List<Chat>> {
                api.getUserChats(user, number, offset)
            }
        }

        data class CreateChatRequest(
            @QueryParam("Chat name")
            val name: String
        )

        fun NormalOpenAPIRoute.createGroupRequest() = userAuthorized {
            post<CreateChatRequest, Chat.Group, Unit> {
                api.createGroup(user, name)
            }
        }

        data class CreatePersonalRequest(
            @QueryParam("User to chat")
            val userId: Long
        )

        fun NormalOpenAPIRoute.createPersonalRequest() = userAuthorized {
            post<CreatePersonalRequest, Chat.Personal, Unit> {
                api.createPersonalChat(user, userId)
            }
        }

        @Path("/{groupId}")
        data class EditGroupRequest(
            @PathParam("Group ID")
            val groupId: Long,
            @QueryParam("New name")
            val name: String?,
            @QueryParam("New chat image")
            val image: String?
        )

        fun NormalOpenAPIRoute.editGroupRequest() = userAuthorized {
            patch<EditGroupRequest, Unit, Unit> {
                api.updateGroupInfo(user, groupId, name, image)
            }
        }

        @Path("/{groupId}")
        data class DeleteGroupRequest(
            @PathParam("Group ID")
            val groupId: Long
        )

        fun NormalOpenAPIRoute.deleteChatRequest() = userAuthorized {
            patch<DeleteGroupRequest, Unit, Unit> {
                api.deleteChat(user, groupId)
            }
        }

        getUsersChatsRequest()
        createGroupRequest()
        createPersonalRequest()
        editGroupRequest()
        deleteChatRequest()

        chatMembers(chatMembersAPI, feature)
        chatMessages(messagesAPI, feature)
    }