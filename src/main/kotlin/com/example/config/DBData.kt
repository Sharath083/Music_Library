package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object DBData {
    private val config= HoconApplicationConfig(ConfigFactory.load())


    val url = config.property("db.url").getString()
    val driver = config.property("db.driver").getString()
    val user = config.property("db.user").getString()
    val password = config.property("db.password").getString()
}
