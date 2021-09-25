package com.dove.android

import android.app.Application
import com.dove.android.locale.ContextualCurrentLocaleProvider
import com.dove.frontend.localization.CurrentLocaleProvider
import kotlin.properties.Delegates

class DoveApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        localeProvider = ContextualCurrentLocaleProvider(applicationContext)
    }

    companion object {
        private var localeProvider: ContextualCurrentLocaleProvider by Delegates.notNull()

        val currentLocaleProvider: CurrentLocaleProvider get() = localeProvider
    }
}