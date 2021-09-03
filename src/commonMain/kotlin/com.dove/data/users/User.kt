package com.dove.data.users

import com.dove.annotations.MaxLength
import com.dove.annotations.MinLength
import com.dove.data.Constants
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    @MinLength(Constants.FIRST_NAME_MIN)
    @MaxLength(Constants.FIRST_NAME_MAX_LEN)
    val firstName: String,
    @MaxLength(Constants.LAST_NAME_MAX_LEN)
    val lastName: String?,
    @MaxLength(Constants.EMAIL_MAX_LEN)
    val email: String
)