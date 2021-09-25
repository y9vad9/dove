package com.dove.frontend.common.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

actual open class ViewModel actual constructor() {
    actual val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
}