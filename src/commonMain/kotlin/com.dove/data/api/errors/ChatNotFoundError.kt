package com.dove.data.api.errors

import kotlinx.serialization.SerialName

@SerialName("ChatNotFoundError")
object ChatNotFoundError : ApiError {
    override val code: Int = 8
    override val message: String = "Chat not found."
}