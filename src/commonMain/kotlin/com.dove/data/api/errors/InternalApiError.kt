package com.dove.data.api.errors

object InternalApiError : ApiError {
    override val code: Int = -1
    override val message: String = "An internal error has occurred."
}