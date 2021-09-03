package com.dove.data.api.errors

object InvalidVerificationError : ApiError {
    override val code: Int = 6
    override val message: String = "Invalid verification code."
}