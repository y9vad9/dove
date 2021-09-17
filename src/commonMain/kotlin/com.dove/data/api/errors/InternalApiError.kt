package com.dove.data.api.errors

import kotlinx.serialization.SerialName

@SerialName("InternalApiError")
object InternalApiError : ApiError {
    override val code: Int = -1
    override val message: String = "An internal error has occurred."
}