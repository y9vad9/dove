package com.dove.server.di

import org.jetbrains.exposed.sql.Database

object DatabaseDI {
    val database = Database.connect(
        url = com.dove.server.Environment.databaseUrl,
        user = com.dove.server.Environment.databaseUser,
        password = com.dove.server.Environment.databasePassword
    )
}