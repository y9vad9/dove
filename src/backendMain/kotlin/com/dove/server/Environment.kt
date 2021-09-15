package com.dove.server

import com.dove.mailer.LocalMailer
import com.dove.mailer.Mailer
import java.nio.file.Path
import kotlin.io.path.Path

object Environment {
    val port: Int by lazy { System.getenv("SERVER_PORT").toIntOrNull() ?: 8080 }
    val debug: Boolean by lazy { System.getenv("SERVER_IS_DEBUG").toBooleanStrictOrNull() ?: true }
    private val isTest: Boolean by lazy { System.getenv("SERVER_IS_TEST").toBooleanStrictOrNull() ?: false }

    val databaseUrl: String by lazy { System.getenv("POSTGRES_URL") }
    val databaseUser: String by lazy { System.getenv("POSTGRES_USER") }
    val databasePassword: String by lazy { System.getenv("POSTGRES_PASSWORD") }

    val mailer: Mailer by lazy { if (isTest) LocalMailer() else TODO() }
    val files: Path by lazy { Path(System.getenv("SERVER_UPLOAD_PATH")) }
}