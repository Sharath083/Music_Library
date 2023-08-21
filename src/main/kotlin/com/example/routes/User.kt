package com.example.routes

import com.example.data.constants.APIConstantsExceptions.SESSION_MESSAGE
import com.example.data.constants.APIConstantsUser.ADD_TO_PLAYLIST
import com.example.data.constants.APIConstantsUser.DELETE_ACCOUNT
import com.example.data.constants.APIConstantsUser.FILTER_BY_ARTIST
import com.example.data.constants.APIConstantsUser.REMOVE_FROM_PLAYLIST
import com.example.data.constants.APIConstantsUser.USER_LOGIN
import com.example.data.constants.APIConstantsUser.USER_LOGOUT
import com.example.data.constants.APIConstantsUser.USER_REGISTRATION
import com.example.data.constants.APIConstantsUser.USER_ROUTES
import com.example.data.constants.APIConstantsUser.VIEW_PLAYLIST
import com.example.data.request.*
import com.example.data.response.Response
import com.example.data.sessiondata.UserDetails
import com.example.data.sessiondata.UserId
import com.example.domain.controller.InterfaceUserImpl
import com.example.domain.exception.SessionDataIsNullException
import io.ktor.http.*
import io.ktor.server.application.*
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
            val result=interfaceUserImpl.userLoginCheck(input.name!!,input.password!!)
            if(result.status==HttpStatusCode.Accepted.toString()){
                call.sessions.set(UserDetails(input.name,input.password))
                call.sessions.set(UserId(interfaceUserImpl.getUserId(input.name)))
            }
            call.respond("$result")
        }
        get(FILTER_BY_ARTIST) {
            val input = call.receive<ArtistData>()
            call.sessions.get<UserId>()?:throw SessionDataIsNullException(SESSION_MESSAGE)
            val result = interfaceUserImpl.filterByArtist(input.artist!!)
            call.respond(result)
        }

        post(ADD_TO_PLAYLIST) {
            val input = call.receive<AddToPlayList>()
            val sessionData=call.sessions.get<UserId>()?:throw SessionDataIsNullException(SESSION_MESSAGE)
            val result = interfaceUserImpl.addToPlayList(input, sessionData.userId!!)
            call.respond(result)
        }
        post(REMOVE_FROM_PLAYLIST) {
            val input = call.receive<RemoveFromPlayList>()
            val sessionData=call.sessions.get<UserId>()?:throw SessionDataIsNullException(SESSION_MESSAGE)
            val result = interfaceUserImpl.removeFromPlayList(input, sessionData.userId!!)
            call.respond(result)
        }

        get(VIEW_PLAYLIST) {
            val input = call.receive<ViewPlayList>()
            val sessionData=call.sessions.get<UserId>()?:throw SessionDataIsNullException(SESSION_MESSAGE)
            val result = interfaceUserImpl.viewPlayList(input.playlistName!!, sessionData.userId!!)
            call.respond(result)
        }

        post(USER_LOGOUT){
            call.sessions.clear<UserDetails>()
            call.sessions.clear<UserId>()
            call.respond(Response.Success("Logout Success",HttpStatusCode.Accepted.toString()))
        }

        delete(DELETE_ACCOUNT) {
            val input=call.sessions.get<UserDetails>()?:throw SessionDataIsNullException(SESSION_MESSAGE)
            val result=interfaceUserImpl.deleteAccount(input.name,input.password)
            call.respond(result)
        }


    }


}