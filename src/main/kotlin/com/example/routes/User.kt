package com.example.routes

import com.example.data.constants.APIConstantsUser.ADD_TO_PLAYLIST
import com.example.data.constants.APIConstantsUser.DELETE_ACCOUNT
import com.example.data.constants.APIConstantsUser.FILTER_BY_ARTIST
import com.example.data.constants.APIConstantsUser.REMOVE_FROM_PLAYLIST
import com.example.data.constants.APIConstantsUser.USER_LOGIN
import com.example.data.constants.APIConstantsUser.USER_REGISTRATION
import com.example.data.constants.APIConstantsUser.USER_ROUTES
import com.example.data.request.*
import com.example.data.sessiondata.UserData
import com.example.data.sessiondata.UserDetails
import com.example.domain.controller.InterfaceUserImpl
import com.example.domain.exception.SessionDataIsNullException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.userRouting(interfaceUserImpl: InterfaceUserImpl){
    route(USER_ROUTES){
        post(USER_REGISTRATION) {
            val regValues=call.receive<UserRegistration>()
            val result=interfaceUserImpl.userRegistration(regValues)
            call.respond(result)
        }
        post (USER_LOGIN){
            val input=call.receive<UserLogin>()
            val result=interfaceUserImpl.userLoginCheck(input.name,input.password)
            if(result.status==HttpStatusCode.Created.toString()){
                call.sessions.clear<UserDetails>()
                call.sessions.clear<UserData>()
                call.sessions.set(UserDetails(input.name,input.password))
                call.sessions.set(UserData(result.response,interfaceUserImpl.getUserId(input.name)))
            }
            call.respond(result)
        }
        authenticate {
            get(FILTER_BY_ARTIST) {
                val input = call.receive<ArtistData>()
                val result = interfaceUserImpl.filterByArtist(input.artist)
                call.respond(result)
            }
        }
        authenticate {
            post(ADD_TO_PLAYLIST) {
                val input = call.receive<AddToPlayList>()
                val sessionData=call.sessions.get<UserData>()?:throw SessionDataIsNullException()
                val result = interfaceUserImpl.addToPlayList(input, sessionData.userId!!)
                call.respond(result)
            }
        }
        authenticate {
            post(REMOVE_FROM_PLAYLIST) {
                val input = call.receive<RemoveFromPlayList>()
                val sessionData=call.sessions.get<UserData>()?:throw SessionDataIsNullException()
                val result = interfaceUserImpl.removeFromPlayList(input, sessionData.userId!!)
                call.respond(result)
            }
        }
        authenticate {
            delete(DELETE_ACCOUNT) {
                val input=call.sessions.get<UserDetails>()?:throw SessionDataIsNullException()
                val result=interfaceUserImpl.deleteAccount(input.name,input.password)
                call.respond(result)
            }
        }

    }


}