package com.dove.backend.server.api.routings.chats.members

import com.dove.backend.apis.chats.members.ChatMembersAPI
import com.dove.backend.server.api.routings.Tags
import com.dove.backend.server.api.routings.users.UsersAuthorizedScope
import com.dove.backend.server.models.ItemsLoadingInfo
import com.dove.data.users.User
import com.papsign.ktor.openapigen.annotations.Path
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import com.y9neon.middleware.authorization.Authorization
import com.y9neon.openapi.get

fun NormalOpenAPIRoute.chatMembers(
    api: ChatMembersAPI,
    authFeature: Authorization.Feature<User>
) = tag(Tags.Members).route("/members") {
    fun userAuthorized(block: UsersAuthorizedScope<NormalOpenAPIRoute>.() -> Unit) {
        block(UsersAuthorizedScope(this, authFeature))
    }

    @Path("/{groupId}")
    data class GetMembersRequest(
        @PathParam("Chat identifier")
        val chatId: Long,
        @QueryParam("Loading info")
        val loadingInfo: ItemsLoadingInfo
    )

    fun NormalOpenAPIRoute.getMembersRequest() = userAuthorized {
        get<GetMembersRequest, List<User>> {
            api.getMembers(user, chatId, loadingInfo.number, loadingInfo.offset)
        }
    }

    @Path("/{chatId}")
    data class MemberingRequest(
        @PathParam("Chat identifier")
        val chatId: Long,
        @QueryParam("User to add identifier")
        val userId: Long
    )

    fun NormalOpenAPIRoute.addMembersRequest() = userAuthorized {
        get<MemberingRequest, Unit> {
            api.addMember(user, chatId, userId)
        }
    }

    fun NormalOpenAPIRoute.kickMembersRequest() = userAuthorized {
        get<MemberingRequest, Unit> {
            api.kickMember(user, chatId, userId)
        }
    }



    getMembersRequest()
    addMembersRequest()
    kickMembersRequest()
}