package com.dove.android.locale

import com.dove.android.DoveApplication
import com.dove.frontend.common.localization.Strings

val Strings: Strings by lazy { Strings(DoveApplication.currentLocaleProvider) }