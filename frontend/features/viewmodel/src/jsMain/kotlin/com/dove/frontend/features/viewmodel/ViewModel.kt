package com.dove.frontend.features.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

actual open class ViewModel {
    actual val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
}