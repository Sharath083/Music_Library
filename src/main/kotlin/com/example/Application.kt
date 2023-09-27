package com.example

import com.example.database.DatabaseFactory
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
fun Application.module() {
    configureKoin()
    configureSerialization()
    DatabaseFactory.init()
    configureSecurity()
    configureStatusPages()
    configureRequestValidation()
    configureRouting()
}
    