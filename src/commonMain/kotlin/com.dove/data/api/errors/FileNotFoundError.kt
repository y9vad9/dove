package com.dove.data.api.errors

import kotlinx.serialization.SerialName

@SerialName("FileNotFoundError")
object FileNotFoundError : ApiError {
    override val code: Int = 7
    override val message: String = "File not found."
}