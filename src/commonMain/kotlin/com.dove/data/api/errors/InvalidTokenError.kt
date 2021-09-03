package com.dove.data.api.errors

class InvalidTokenError(message: String = "No such token exist") : ApiError {
    override val code: Int = 5
    override val message: String = "Token is invalid: $message"
}