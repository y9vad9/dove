package com.dove.data.chats

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val memberId: Long,
    val memberType: MemberType
)