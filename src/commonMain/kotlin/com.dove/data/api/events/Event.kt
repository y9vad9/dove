package com.dove.data.api.events

import com.dove.data.users.User

sealed interface Event {
    /**
     * User that should be notified.
     */
    val user: User
}