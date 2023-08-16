package com.example.plugins

import com.example.domain.controller.InterfaceAdminImpl
import com.example.domain.controller.InterfaceUserImpl
import com.example.routes.adminFunctions
import com.example.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        adminFunctions(InterfaceAdminImpl())
        userRouting(InterfaceUserImpl())

    }

}
