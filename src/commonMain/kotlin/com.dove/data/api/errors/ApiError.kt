package com.dove.data.api.errors

sealed interface ApiError {
    val code: Int
    val message: String
}