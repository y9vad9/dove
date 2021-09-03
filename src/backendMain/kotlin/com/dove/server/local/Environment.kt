package com.dove.server.local

import com.dove.mailer.LocalMailer
import com.dove.mailer.Mailer
import com.dove.mailer.SMTPCredentials
import com.dove.mailer.SMTPMailer

object Environment {
    val port: Int by lazy { System.getenv("server.port").toIntOrNull() ?: 8080 }
    val debug: Boolean by lazy { System.getenv("server.debug").toBooleanStrictOrNull() ?: true }
    val isTest: Boolean by lazy { System.getenv("server.isTest").toBooleanStrictOrNull() ?: false }

    val databaseUrl: String by lazy { System.getenv("database.url") }
    val databaseUser: String by lazy { System.getenv("database.user") }
    val databasePassword: String by lazy { System.getenv("database.password") }

    val mailer: Mailer = if(isTest) LocalMailer() else TODO()
}