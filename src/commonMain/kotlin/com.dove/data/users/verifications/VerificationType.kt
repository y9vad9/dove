package com.dove.data.users.verifications

import kotlinx.serialization.Serializable

@Serializable
enum class VerificationType {
    /**
     * Verification for getting token.
     */
    AUTH,
    REGISTRATION
}