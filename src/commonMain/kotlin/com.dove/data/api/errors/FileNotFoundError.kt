package com.dove.data.api.errors

object FileNotFoundError : ApiError {
    override val code: Int = 7
    override val message: String = "File not found."
}