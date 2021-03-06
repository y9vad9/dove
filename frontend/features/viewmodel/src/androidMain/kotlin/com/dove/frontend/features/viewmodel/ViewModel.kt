package com.dove.frontend.features.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual open class ViewModel : ViewModel() {
    actual val scope: CoroutineScope = viewModelScope
}