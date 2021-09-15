package com.dove.server.di

import com.dove.server.Environment
import org.jetbrains.exposed.sql.Database

object DatabaseDI {
    @JvmStatic
    val database = Database.connect(
        url = Environment.databaseUrl,
        user = Environment.databaseUser,
        password = Environment.databasePassword
    )
}