package com.dove.mailer

class SMTPCredentials(
    val address: String,
    val port: Int,
    val email: String,
    val password: String
)