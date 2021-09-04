package com.dove.data.api.errors

object MessageNotFoundError : ApiError {
    override val code: Int = 10
    override val message: String = "Message not found."
}