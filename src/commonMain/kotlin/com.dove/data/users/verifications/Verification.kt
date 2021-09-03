package com.dove.data.users.verifications

import kotlinx.serialization.Serializable

@Serializable
class Verification(
    val email: String,
    val code: String,
    val type: VerificationType,
    val time: Long
)