package com.dove.android.locale

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.dove.frontend.features.localization.CurrentLocaleProvider
import com.dove.frontend.features.localization.Locale

class ContextualCurrentLocaleProvider(private val context: Context) : CurrentLocaleProvider {
    override fun provide(): Locale =
        ConfigurationCompat.getLocales(context.resources.configuration)[0].let { Locale(it.country, it.language) }
}