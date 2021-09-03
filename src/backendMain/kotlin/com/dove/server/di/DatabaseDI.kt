package com.dove.server.di

import com.dove.server.local.Environment
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