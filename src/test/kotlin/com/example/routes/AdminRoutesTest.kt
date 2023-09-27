package com.example.routes

import com.example.utils.appconstant.APIEndPoints.ADMIN_LOGIN
import com.example.utils.appconstant.APIEndPoints.ADMIN_ROUTES
import com.example.model.AdminLogin
import com.example.model.DeleteSong
import com.example.model.InputSong
import com.example.module
import com.example.service.AdminServices
import com.example.utils.appconstant.APIEndPoints.ADD_NEW_SONG
import com.example.utils.appconstant.APIEndPoints.REMOVE_SONG
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.netty.handler.codec.http.HttpHeaders.addHeader
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class AdminRoutesTest {
    private lateinit var token:String
    @Test
    fun adminLoginTest(): Unit = testApplication {
        val adminLogin = AdminLogin("admin", "1234")
        val serializedData = Json.encodeToString(adminLogin)
            val response = client.post(ADMIN_ROUTES + ADMIN_LOGIN){
                headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                setBody(serializedData)
            }
//        token=response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun addNewSong(): Unit = testApplication {
        val input= InputSong("colds", "coldplay", "3:47")
        val serializedData = Json.encodeToString(input)
//        val token=AdminServices().adminLoginService("admin","1234").response
        val response = client.post(ADMIN_ROUTES + ADD_NEW_SONG ){
//            bearerAuth(token)
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(serializedData)
        }
        assertEquals(HttpStatusCode.Accepted, response.status)
    }
    @Test
    fun deleteSongTest(): Unit = testApplication {
        val deleteSong = DeleteSong("colds", "coldplay")
        val serializedData = Json.encodeToString(deleteSong)
        val response = client.delete(ADMIN_ROUTES + REMOVE_SONG){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(serializedData)
        }
        assertEquals(HttpStatusCode.Accepted, response.status)
    }
}


