package com.example.routes

import com.example.exceptions.SessionDataIsNullException
import com.example.model.*
import com.example.utils.appconstant.APIEndPoints
import com.example.utils.appconstant.APIEndPoints.FILTER_BY_ARTIST
import com.example.utils.appconstant.APIEndPoints.USER_LOGIN
import com.example.utils.appconstant.APIEndPoints.USER_REGISTRATION
import com.example.utils.appconstant.APIEndPoints.USER_ROUTES
import com.example.utils.appconstant.InfoMessage
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import io.netty.handler.codec.http.HttpHeaders.addHeader
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

//class KtorTest {
//    @Test
//    fun test(): Unit = testApplication {
//        application {
//            install(Sessions) {
//                cookie<User>(
//                    "cookie-name",
//                    directorySessionStorage(File(".sessions"), cached = true)
//                )
//            }
//
//            routing {
//                post("/sign") {
//                    call.sessions.set(User("123"))
//                }
//
//                get("/user") {
//                    call.respond(HttpStatusCode.OK)
//                }
//            }
//        }
//
//        val setCookieHeader = client.post("/sign").headers[HttpHeaders.SetCookie]
//        assertNotNull(setCookieHeader)
//
//        val c = parseServerSetCookieHeader(setCookieHeader)
//        val response = client.get("/user") {
//            cookie(c.name, c.value, path = c.path)
//        }
//        assertNull(response.headers[HttpHeaders.SetCookie]) // this assertion fails
//    }
//}

//data class User(val id: String)

class UserRoutesTest {
    private val register=UserRegistration("sharath5","sharath5@gmail.com","12345")
    private val userLogin=UserLogin("sharath5","12345")
    private val input=InputSong("cold", "coldplay", "3:47")
    private val playList=AddToPlayList("cold", "my1")
    private val removePlayList=RemoveFromPlayList("cold", "my1")
    private val artistData=ArtistData("coldplay")
    @Test
    fun userRegistrationRouteTest()= testApplication {
        val serializedData = Json.encodeToString(register)
        val response = client.post(USER_ROUTES + USER_REGISTRATION){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(serializedData)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }
    @Test
    fun userLoginRouteTest()= testApplication {
        val serializedData = Json.encodeToString(userLogin)
        val response = client.post(USER_ROUTES + USER_LOGIN){
            headers[HttpHeaders.SetCookie]
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(serializedData)
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun filterByArtistRouteTest()= testApplication {
        val serializedData = Json.encodeToString(artistData)
        val response = client.get(USER_ROUTES + FILTER_BY_ARTIST){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(serializedData)
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }
//    post(USER_REGISTRATION) {
//        val regValues=call.receive<UserRegistration>()
//        userServices.userRegistrationService(regValues)
//            .apply {
//                call.respond(HttpStatusCode.OK,this) }
//    }
//    post (USER_LOGIN){
//        val input=call.receive<UserLogin>()
//        userServices.userLoginCheckService(input)
//            .apply {
//                userServices.getUserIdService(input.name!!).apply {
//                    call.sessions.set(UserId(this))
//                }
//                call.respond(HttpStatusCode.Created, this)
//            }
//    }
//    get(FILTER_BY_ARTIST) {
//        val input = call.receive<ArtistData>()
//        call.sessions.get<UserId>()
//            ?:throw SessionDataIsNullException(InfoMessage.SESSION_MESSAGE,HttpStatusCode.Unauthorized)
//        userServices.filterByArtistService(input.artist!!)
//            .apply {
//                call.respond(HttpStatusCode.OK,this) }
//    }
//
//    post(ADD_TO_PLAYLIST) {
//        val input = call.receive<AddToPlayList>()
//        val sessionData = call.sessions.get<UserId>()
//            ?: throw SessionDataIsNullException(InfoMessage.SESSION_MESSAGE, HttpStatusCode.Unauthorized)
//        userServices.addSongToPlayListService(input, sessionData.userId)
//            .apply {
//                call.respond(HttpStatusCode.OK, this)
//            }
//    }
//    post(REMOVE_FROM_PLAYLIST) {
//        val input = call.receive<RemoveFromPlayList>()
//        val sessionData=call.sessions.get<UserId>()
//            ?:throw SessionDataIsNullException(InfoMessage.SESSION_MESSAGE,HttpStatusCode.Unauthorized)
//        userServices.removeFromPlayListService(input, sessionData.userId)
//            .apply {
//                call.respond(HttpStatusCode.OK,this) }
//    }
//
//    get(VIEW_PLAYLIST) {
//        val input = call.receive<ViewPlayList>()
//        val sessionData=call.sessions.get<UserId>()
//            ?:throw SessionDataIsNullException(InfoMessage.SESSION_MESSAGE,HttpStatusCode.Unauthorized)
//        userServices.viewPlayListService(input.playlistName!!, sessionData.userId)
//            .apply {
//                call.respond(HttpStatusCode.OK,this) }
//    }
//    post(DELETE_PLAYLIST) {
//        val input = call.receive<ViewPlayList>()
//        val sessionData=call.sessions.get<UserId>()?:throw SessionDataIsNullException(InfoMessage.SESSION_MESSAGE,HttpStatusCode.Unauthorized)
//        userServices.deletePlayListService(input.playlistName!!, sessionData.userId)
//            .apply {
//                call.respond(HttpStatusCode.OK,this) }
//    }
//
//    post(USER_LOGOUT){
//        call.sessions.clear<UserId>()
//        call.respond(HttpStatusCode.OK, SuccessResponse("Logout Success",HttpStatusCode.Accepted.toString()))
//    }
//
//    delete(DELETE_ACCOUNT) {
//        val input=call.sessions.get<UserId>()
//            ?:throw SessionDataIsNullException(InfoMessage.SESSION_MESSAGE,HttpStatusCode.Unauthorized)
//        userServices.deleteAccountService(input.userId)
//            .apply {
//                call.sessions.clear<UserId>()
//                call.respond(HttpStatusCode.OK,this)
//            }
//    }

}
