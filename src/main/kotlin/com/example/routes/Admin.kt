package com.example.routes

import com.example.data.constants.APIConstantsAdmin.ADD_NEW_SONG
import com.example.data.constants.APIConstantsAdmin.ADMIN_LOGIN
import com.example.data.constants.APIConstantsAdmin.ADMIN_ROUTES
import com.example.data.constants.APIConstantsAdmin.REMOVE_SONG
import com.example.data.request.AdminLogin
import com.example.data.request.DeleteSong
import com.example.data.request.InputSong
import com.example.domain.controller.InterfaceAdminImpl
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminFunctions(interfaceImpl: InterfaceAdminImpl){
    route(ADMIN_ROUTES){

        post(ADMIN_LOGIN) {
            val input=call.receive<AdminLogin>()
            val result=interfaceImpl.adminLoginCheck(input.name,input.password)
            call.respond(result)
        }
        authenticate("Admin") {
            post(ADD_NEW_SONG) {
                val input = call.receive<InputSong>()
                val result = interfaceImpl.addSong(input)
                call.respond("$result")
            }
        }
        authenticate("Admin") {
            delete(REMOVE_SONG) {
                val input = call.receive<DeleteSong>()
                val result = interfaceImpl.deleteSong(input)
                call.respond(result)
            }
        }

    }

}

