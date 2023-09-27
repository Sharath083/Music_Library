package com.example

import com.example.model.AdminLogin
import com.example.utils.appconstant.APIEndPoints.ADMIN_LOGIN
import com.example.utils.appconstant.APIEndPoints.ADMIN_ROUTES
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun adminLoginRoot()= testApplication {
        val user= AdminLogin("admin","1234")
        val response = client.get(ADMIN_ROUTES + ADMIN_LOGIN) {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(user)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
