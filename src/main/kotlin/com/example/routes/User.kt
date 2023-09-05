package com.example.routes

import com.example.utils.appconstant.InfoMessage.SESSION_MESSAGE
import com.example.data.model.*
import com.example.data.model.UserId
import com.example.repositories.InterfaceUserImpl
import com.example.service.UserServices
import com.example.utils.SessionDataIsNullException
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
    val userServices: UserServices by inject()
    route(USER_ROUTES){
        post(USER_REGISTRATION) {
            val regValues=call.receive<UserRegistration>()
            userServices.userRegistrationService(regValues)
                .apply {
                    call.respond(HttpStatusCode.OK,this) }
        }
        post (USER_LOGIN){
            val input=call.receive<UserLogin>()
            userServices.userLoginCheckService(input.name!!,input.password!!)
                .apply {
                    val id = userServices.getUserIdService(input.name)
                    call.sessions.set(UserId(id))
                    call.respond(HttpStatusCode.Created, this)
                }


        }
        get(FILTER_BY_ARTIST) {
            val input = call.receive<ArtistData>()
            call.sessions.get<UserId>()
                ?:throw SessionDataIsNullException(SESSION_MESSAGE,HttpStatusCode.Unauthorized)
            userServices.filterByArtistService(input.artist!!)
                .apply {
                    call.respond(HttpStatusCode.OK,this) }
        }

        post(ADD_TO_PLAYLIST) {
            val input = call.receive<AddToPlayList>()
            val sessionData=call.sessions.get<UserId>()
                ?:throw SessionDataIsNullException(SESSION_MESSAGE,HttpStatusCode.Unauthorized)
            userServices.addToPlayList(input, sessionData.userId!!)
                .apply {
                    call.respond(HttpStatusCode.OK,this) }
        }
        post(REMOVE_FROM_PLAYLIST) {
            val input = call.receive<RemoveFromPlayList>()
            val sessionData=call.sessions.get<UserId>()
                ?:throw SessionDataIsNullException(SESSION_MESSAGE,HttpStatusCode.Unauthorized)
            userServices.removeFromPlayListService(input, sessionData.userId!!)
                .apply {
                    call.respond(HttpStatusCode.OK,this) }
        }

        get(VIEW_PLAYLIST) {
            val input = call.receive<ViewPlayList>()
            val sessionData=call.sessions.get<UserId>()?:throw SessionDataIsNullException(SESSION_MESSAGE,HttpStatusCode.Unauthorized)
            userServices.viewPlayListService(input.playlistName!!, sessionData.userId!!)
                .apply {
                    call.respond(HttpStatusCode.OK,this) }        }

        post(USER_LOGOUT){
            call.sessions.clear<UserId>()
            call.respond(HttpStatusCode.OK,SuccessResponse("Logout Success",HttpStatusCode.Accepted.toString()))
        }

        delete(DELETE_ACCOUNT) {
            val input=call.sessions.get<UserId>()
                ?:throw SessionDataIsNullException(SESSION_MESSAGE,HttpStatusCode.Unauthorized)
            userServices.deleteAccountService(input.userId!!)
                .apply {
                    call.sessions.clear<UserId>()
                    call.respond(HttpStatusCode.OK,this)
                }

        }


    }


}