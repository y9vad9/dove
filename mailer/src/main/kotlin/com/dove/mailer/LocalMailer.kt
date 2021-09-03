package com.dove.mailer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext

/**
 * Mailer implementation for test purposes.
 */
class LocalMailer : Mailer {
    private val _emails: MutableSharedFlow<Email> = MutableSharedFlow()

    val emails: SharedFlow<Email> get() = _emails

    override suspend fun send(email: Email): Boolean = withContext(Dispatchers.IO) {
        _emails.emit(email)
        return@withContext true
    }
}