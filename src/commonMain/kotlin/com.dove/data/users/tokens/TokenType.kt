package com.dove.data.users.tokens

import kotlinx.serialization.Serializable

@Serializable
enum class TokenType {
    /**
     * This type of token used only for registration (setting account, etc.).
     */
    REGISTRATION,

    /**
     * This type of token can be used for regular stuff (sending messages, etc.)
     */
    REGULAR,

    /**
     * This type of token can be used only for managing account (for deleting account, etc.)
     */
    MANAGING
}