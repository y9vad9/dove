package com.dove.data.api.errors

import kotlinx.serialization.SerialName

@SerialName("NoSuchPermissionError")
class NoSuchPermissionError(message: String) : ApiError {
    override val code: Int = 9
    override val message: String = "Unable to issue this operation: $message"
}