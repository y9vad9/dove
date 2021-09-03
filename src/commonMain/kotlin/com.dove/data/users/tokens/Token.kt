package com.dove.data.users.tokens

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val tokenId: Long,
    val userId: Long,
    val token: String,
    val time: Long,
    val type: TokenType
)