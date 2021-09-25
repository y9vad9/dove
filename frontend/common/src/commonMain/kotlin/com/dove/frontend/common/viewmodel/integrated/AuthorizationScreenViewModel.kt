package com.dove.frontend.common.viewmodel.integrated

import com.dove.data.api.ApiError
import com.dove.frontend.common.viewmodel.AuthorizationViewModel
import com.dove.frontend.features.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthorizationScreenViewModel : AuthorizationViewModel, ViewModel() {
    override val stage: MutableStateFlow<AuthorizationViewModel.Stage> =
        MutableStateFlow(AuthorizationViewModel.Stage.EmailInput)
    override val email: MutableStateFlow<String> = MutableStateFlow("")
    override val code: MutableStateFlow<String> = MutableStateFlow("")
    override val errors: MutableSharedFlow<ApiError> = MutableSharedFlow()

    override fun processEmail(email: String) {
        this.email.value = email
    }

    override fun processCode(code: String) {
        this.code.value = code
    }

    override fun sendEmail() {
        TODO("Not yet implemented")
    }

    override fun sendCode() {
        TODO("Not yet implemented")
    }

}