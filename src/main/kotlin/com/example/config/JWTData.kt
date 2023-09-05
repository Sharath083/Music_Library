package com.example.config

import io.ktor.server.config.*
import com.typesafe.config.ConfigFactory

object JWTData{

    private val config=HoconApplicationConfig(ConfigFactory.load())


    val secretUser = config.property("jwt.secretUser").getString()
    val secretAdmin = config.property("jwt.secretAdmin").getString()
    val issuer = config.property("jwt.issuer").getString()
    val audience = config.property("jwt.audience").getString()
    val realm = "Unauthorized"


}