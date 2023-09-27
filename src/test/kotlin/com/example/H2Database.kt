package com.example

import org.jetbrains.exposed.sql.Database


object H2Database {
    fun init(): Database {
        return  Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver")

    }
}