package com.dove.data.chats.updates

import kotlinx.serialization.Serializable

@Serializable
data class UnsubscribeFromChats(val token: String, val ids: Set<Long>)