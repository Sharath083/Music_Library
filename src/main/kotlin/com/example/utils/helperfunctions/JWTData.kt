package com.example.utils.helperfunctions

import io.ktor.server.application.*
import io.ktor.server.config.*
import com.typesafe.config.ConfigFactory

object JWTData{
//    val secretUser = "login user"
//    val secretAdmin = "admin login"
//    val issuer = "http://0.0.0.0:8080/"
//    val audience = "http://0.0.0.0:8080/hello"
//    val realm = "Unauthorized"
    private val config=HoconApplicationConfig(ConfigFactory.load())


    val secretUser = config.property("jwt.secretUser").getString()
    val secretAdmin = config.property("jwt.secretAdmin").getString()
    val issuer = config.property("jwt.issuer").getString()
    val audience = config.property("jwt.audience").getString()
    val realm = "Unauthorized"


}