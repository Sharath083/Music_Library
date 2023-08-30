package com.example

import com.example.data.model.AdminLogin
import com.example.utils.appconstant.APIEndPoints.ADMIN_LOGIN
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {

    @Test
    fun adminLoginRoot()= testApplication {
        val user= AdminLogin("admina","1234")
        val response = client.get(ADMIN_LOGIN) {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(user)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
