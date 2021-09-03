package com.dove.mailer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.mail.SimpleEmail

class SMTPMailer(private val credentials: SMTPCredentials) : Mailer {
    override suspend fun send(email: Email): Boolean = withContext(Dispatchers.IO) {
        SimpleEmail().apply {
            isSSLOnConnect = true
            hostName = credentials.address

            setSmtpPort(credentials.port)
            setAuthentication(credentials.email, credentials.password)
            setFrom(credentials.email)

            subject = email.subject
            setMsg(email.message)
            addTo(*email.receivers.toTypedArray())
            updateContentType("text/html")
        }.send() ?: return@withContext false
        return@withContext true
    }
}