package com.dove.frontend.features.localization

interface CurrentLocaleProvider {
    /**
     * @return current [Locale].
     */
    fun provide(): Locale
}