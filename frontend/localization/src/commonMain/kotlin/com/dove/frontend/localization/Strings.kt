package com.dove.frontend.localization

import kotlin.properties.ReadOnlyProperty

/**
 * Frontend localized strings.
 */
open class Strings(private val currentLocaleProvider: CurrentLocaleProvider) {
    val authorization by localized("Authorization") {
        "Авторизация" to Locale.ru
        "Авторизація" to Locale.uk
    }
    val email by localized("Email") {
        "Электронная почта" to Locale.ru
        "Електронна пошта" to Locale.uk
    }
    val code by localized("Code") {
        "Код" to Locale.ru
        "Код" to Locale.uk
    }
    val codeWasSentToEmail by localized("Code was sent to your email for authorization confirmation.") {
        "На вашу почту был отправлен код для подтверждения авторизации." to Locale.ru
        "На вашу пошту був відправленний код для підтверження авторизації." to Locale.uk
    }
    val loading by localized("Loading") {
        "Загрузка" to Locale.ru
        "Завантаження" to Locale.uk
    }
    val message by localized("Message") {
        "Сообщение" to Locale.ru
        "Повідомлення" to Locale.uk
    }
    val send by localized("Send") {
        "Отправить" to Locale.ru
        "Відправити" to Locale.uk
    }
    val invalidCodeError by localized("Invalid code") {
        "Неправильный код" to Locale.ru
        "Невірний код" to Locale.uk
    }

    private fun localized(default: String, block: Localized<String>.() -> Unit): ReadOnlyProperty<Any?, String> {
        return localized(default, currentLocaleProvider, block)
    }
}