package com.dove.data.api.errors

import kotlinx.serialization.SerialName

@SerialName("MessageNotFound")
object MessageNotFoundError : ApiError {
    override val code: Int = 10
    override val message: String = "Message not found."
}