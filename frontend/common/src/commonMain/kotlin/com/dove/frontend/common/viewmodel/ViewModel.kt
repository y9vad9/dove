package com.dove.frontend.common.viewmodel

import kotlinx.coroutines.CoroutineScope

/**
 * Base class of any view model
 */
expect open class ViewModel() {
    val scope: CoroutineScope
}