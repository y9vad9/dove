package com.dove.frontend.localization

interface CurrentLocaleProvider {
    /**
     * @return current [Locale].
     */
    fun provide(): Locale
}