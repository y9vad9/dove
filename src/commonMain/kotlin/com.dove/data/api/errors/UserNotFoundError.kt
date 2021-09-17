package com.dove.data.api.errors

import kotlinx.serialization.SerialName

@SerialName("UserNotFoundError")
object UserNotFoundError : ApiError {
    override val code: Int = 4
    override val message: String = "User not found."
}