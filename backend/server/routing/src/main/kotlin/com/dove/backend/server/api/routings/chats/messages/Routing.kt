package com.dove.backend.server.api.routings.chats.messages

import com.dove.backend.apis.chats.messages.MessagesAPI
import com.dove.backend.server.api.routings.Tags
import com.dove.backend.server.api.routings.users.UsersAuthorizedScope
import com.dove.backend.server.models.ItemsLoadingInfo
import com.dove.data.chats.messages.Message
import com.dove.data.chats.messages.MessageType
import com.dove.data.users.User
import com.papsign.ktor.openapigen.annotations.Path
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import com.y9neon.middleware.authorization.Authorization
import com.y9neon.openapi.authorized
import com.y9neon.openapi.delete
import com.y9neon.openapi.get
import com.y9neon.openapi.post

fun NormalOpenAPIRoute.chatMessages(api: MessagesAPI, authFeature: Authorization.Feature<User>) =
    tag(Tags.Messages).route("/messages") {
        fun userAuthorized(block: UsersAuthorizedScope<NormalOpenAPIRoute>.() -> Unit) {
            authorized(authFeature) {
                block(UsersAuthorizedScope(this, authFeature))
            }
        }

        @Path("/{chatId}")
        data class GetMessagesRequest(
            @PathParam("Chat identifier")
            val chatId: Long,
            @QueryParam("Loading information")
            val loadingInfo: ItemsLoadingInfo
        )

        fun NormalOpenAPIRoute.getMessagesRequest() = userAuthorized {
            get<GetMessagesRequest, List<Message>> {
                api.getMessages(user, chatId, loadingInfo.number, loadingInfo.offset)
            }
        }

        @Path("/{chatId}")
        data class SendMessagesRequest(
            @PathParam("Chat identifier")
            val chatId: Long,
            @QueryParam("Loading information")
            val message: String,
            @QueryParam("Message type")
            val type: MessageType
        )

        fun NormalOpenAPIRoute.sendMessagesRequest() = userAuthorized {
            post<SendMessagesRequest, Unit, Unit> {
                api.sendMessage(user, chatId, message, type)
            }
        }

        @Path("/{chatId}")
        data class DeleteMessagesRequest(
            @PathParam("Chat identifier")
            val chatId: Long,
            @QueryParam("Loading information")
            val messageId: Long
        )

        fun NormalOpenAPIRoute.deleteMessagesRequest() = userAuthorized {
            delete<DeleteMessagesRequest, Unit> {
                api.deleteMessage(user, chatId, messageId)
            }
        }

        getMessagesRequest()
        sendMessagesRequest()
        deleteMessagesRequest()
    }