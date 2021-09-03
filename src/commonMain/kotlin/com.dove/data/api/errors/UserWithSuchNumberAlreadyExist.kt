package com.dove.data.api.errors

object UserWithSuchNumberAlreadyExist : ApiError {
    override val code: Int = 2
    override val message: String = "User with such number already exist."
}