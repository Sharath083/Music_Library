package com.example.routes

import com.example.utils.appconstant.APIEndPoints.ADD_NEW_SONG
import com.example.utils.appconstant.APIEndPoints.ADMIN_LOGIN
import com.example.utils.appconstant.APIEndPoints.ADMIN_ROUTES
import com.example.utils.appconstant.APIEndPoints.REMOVE_SONG
import com.example.data.model.AdminLogin
import com.example.data.model.DeleteSong
import com.example.data.model.InputSong
import com.example.data.repositories.InterfaceAdminImpl
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.adminFunctions(){
    val interfaceImpl: InterfaceAdminImpl by inject()
    route(ADMIN_ROUTES){

        post(ADMIN_LOGIN) {
            val input=call.receive<AdminLogin>()
            val result=interfaceImpl.adminLoginCheck(input.name!!,input.password!!)
            call.respond(result)
        }
        authenticate("Admin") {
            post(ADD_NEW_SONG) {
                val input = call.receive<InputSong>()
                val result = interfaceImpl.addSong(input)
                call.respond("$result")
            }
            delete(REMOVE_SONG) {
                val input = call.receive<DeleteSong>()
                val result = interfaceImpl.deleteSong(input)
                call.respond(result)
            }
        }
    }

}

