package com.dove.frontend.localization

import kotlin.properties.ReadOnlyProperty

/**
 * Class-wrapper for localization [T].
 * @param default - default localized object.
 */
data class Localized<T> internal constructor(
    val default: T,
    private val _variants: MutableMap<Locale, T> = mutableMapOf()
) {

    constructor(default: T) : this(default, mutableMapOf())

    val variants: Map<Locale, T> get() = _variants.toMap()

    fun appendVariant(locale: Locale, value: T) {
        _variants[locale] = value
    }

    infix fun T.to(locale: Locale): Unit =
        appendVariant(locale, this)
}

operator fun <T> Localized<T>.get(locale: Locale): T =
    variants[locale] ?: default

/**
 * Builder for [Localized].
 * @param default - default localized object.
 */
fun <T> localized(
    default: T,
    currentLocaleProvider: CurrentLocaleProvider,
    builder: Localized<T>.() -> Unit
): ReadOnlyProperty<Any?, T> {
    val localized = Localized(default).apply(builder)
    return ReadOnlyProperty { _, _ ->
        return@ReadOnlyProperty localized[currentLocaleProvider.provide()]
    }
}