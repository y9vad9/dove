package com.dove.data.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: Int,
    val message: String
) {
    /**
     * All errors.
     */
    companion object {
        val ChatNotFoundError = ApiError(8, "Chat not found")
        val FileNotFoundError = ApiError(7, "File not found error.")
        val InternalServerError = ApiError(-1, "Internal server error.")
        val InvalidTokenError = ApiError(6, "Invalid token error.")
        val InvalidVerificationError = ApiError(5, "Invalid Verification error")
        val MessageNotFoundError = ApiError(4, "Message not found error.")
        val NoSuchPermissionError = ApiError(3, "No such permission.")
        val UserNotFoundError = ApiError(2, "User not found error")

        /**
         * Makes error with next message: [message].
         * @param message - message to show.
         */
        fun permissionError(message: String): ApiError = ApiError(NoSuchPermissionError.code, message)
    }
}