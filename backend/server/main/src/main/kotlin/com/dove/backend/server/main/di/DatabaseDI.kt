package com.dove.backend.server.main.di

import com.dove.backend.server.main.Environment
import org.jetbrains.exposed.sql.Database

object DatabaseDI {
    val database by lazy {
        Database.connect(
            url = Environment.databaseUrl,
            user = Environment.databaseUser,
            password = Environment.databasePassword
        )
    }
}