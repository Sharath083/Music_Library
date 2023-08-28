package com.example.routes

import com.example.utils.appconstant.InfoMessage.SESSION_MESSAGE
import com.example.data.model.*
import com.example.data.model.Response
import com.example.data.model.UserId
import com.example.data.repositories.InterfaceUserImpl
import com.example.domain.exception.SessionDataIsNullException
import com.example.utils.appconstant.APIEndPoints.ADD_TO_PLAYLIST
import com.example.utils.appconstant.APIEndPoints.DELETE_ACCOUNT
import com.example.utils.appconstant.APIEndPoints.FILTER_BY_ARTIST
import com.example.utils.appconstant.APIEndPoints.REMOVE_FROM_PLAYLIST
import com.example.utils.appconstant.APIEndPoints.USER_LOGIN
import com.example.utils.appconstant.APIEndPoints.USER_LOGOUT
import com.example.utils.appconstant.APIEndPoints.USER_REGISTRATION
import com.example.utils.appconstant.APIEndPoints.USER_ROUTES
import com.example.utils.appconstant.APIEndPoints.VIEW_PLAYLIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.userRouting(){
    val interfaceUserImpl: InterfaceUserImpl by inject()
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
                val id=interfaceUserImpl.getUserId(input.name)
                call.sessions.set(UserId(id))
            }
            call.respond(result)
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
            call.sessions.clear<UserId>()
            call.respond(Response.Success("Logout Success",HttpStatusCode.Accepted.toString()))
        }

        delete(DELETE_ACCOUNT) {
            val input=call.sessions.get<UserId>()?:throw SessionDataIsNullException(SESSION_MESSAGE)
            val result=interfaceUserImpl.deleteAccount(input.userId!!)
            call.respond(result)
        }


    }


}