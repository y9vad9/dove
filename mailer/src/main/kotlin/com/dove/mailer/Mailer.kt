package com.dove.mailer

interface Mailer {
    /**
     * Sends email to specified addresses.
     */
    suspend fun send(email: Email): Boolean
}

suspend fun Mailer.send(title: String, subject: String, receiver: String): Boolean =
    send(Email(title, subject, receiver))