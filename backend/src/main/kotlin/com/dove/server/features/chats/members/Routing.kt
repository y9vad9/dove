package com.dove.server.features.chats.members

import com.dove.data.users.User
import com.dove.server.features.Tags
import com.dove.server.features.chats.members.storage.ChatMembersStorage
import com.dove.server.features.models.ItemsLoadingInfo
import com.dove.server.features.users.storage.UsersStorage
import com.dove.server.utils.openapi.get
import com.dove.server.utils.openapi.user
import com.dove.server.utils.openapi.userAuthorized
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag

private val api: ChatMembersAPI = ChatMembersAPI(ChatMembersStorage.Default, UsersStorage.Default)

fun NormalOpenAPIRoute.chatMembers() = tag(Tags.Members).route("/members") {
    getMembersRequest()
    addMembersRequest()
    kickMembersRequest()
}

private data class GetMembersRequest(
    @QueryParam("Chat identifier")
    val chatId: Long,
    @QueryParam("Loading info")
    val loadingInfo: ItemsLoadingInfo
)

private fun NormalOpenAPIRoute.getMembersRequest() = userAuthorized {
    get<GetMembersRequest, List<User>> {
        api.getMembers(user, chatId, loadingInfo.number, loadingInfo.offset)
    }
}

private data class MemberingRequest(
    @QueryParam("Chat identifier")
    val chatId: Long,
    @QueryParam("User to add identifier")
    val userId: Long
)

private fun NormalOpenAPIRoute.addMembersRequest() = userAuthorized {
    get<MemberingRequest, Unit> {
        api.addMember(user, chatId, userId)
    }
}

private fun NormalOpenAPIRoute.kickMembersRequest() = userAuthorized {
    get<MemberingRequest, Unit> {
        api.kickMember(user, chatId, userId)
    }
}

