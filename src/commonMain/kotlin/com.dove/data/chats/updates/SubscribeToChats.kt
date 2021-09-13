package com.dove.data.chats.updates

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeToChats(val token: String, val ids: Set<Long>)