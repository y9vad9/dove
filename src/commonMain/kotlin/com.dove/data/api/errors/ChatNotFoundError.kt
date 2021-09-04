package com.dove.data.api.errors

object ChatNotFoundError : ApiError {
    override val code: Int = 8
    override val message: String = "Chat not found."
}