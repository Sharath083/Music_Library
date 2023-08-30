package com.example.plugins

import com.example.repositories.InterfaceAdminImpl
import com.example.repositories.InterfaceUserImpl
import com.example.routes.adminFunctions
import com.example.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        adminFunctions()
        userRouting()
    }

}
