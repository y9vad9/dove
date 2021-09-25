package com.dove.frontend.common.viewmodel

import com.dove.data.api.ApiError
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthorizationViewModel {
    val stage: StateFlow<Stage>
    val email: StateFlow<String>
    val code: StateFlow<String>
    val errors: SharedFlow<ApiError>

    /**
     * Receives new email input and emitting it into [email].
     */
    fun processEmail(email: String)

    /**
     * Receives new code input and emits it into [code].
     */
    fun processCode(code: String)

    /**
     * Sends request for token with [email].
     */
    fun sendEmail()

    /**
     * Sends request for getting token with [code] and [email].
     */
    fun sendCode()

    /**
     * Authorization stages.
     */
    enum class Stage {
        EmailInput, CodeInput
    }
}