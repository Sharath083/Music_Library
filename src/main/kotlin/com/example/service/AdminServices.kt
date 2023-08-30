package com.example.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.model.SuccessResponse
import com.example.utils.InvalidLoginForAdminException
import com.example.utils.helperfunctions.JWTData
import io.ktor.http.*
import java.util.*

class AdminServices {
    private val secretAdmin = JWTData.secretAdmin
    private val issuer = JWTData.issuer
    private val audience = JWTData.audience
    fun adminLoginCheck(name:String, password:String): SuccessResponse {
        return if(name==("admin") && password=="1234"){
            val token=tokenGeneratorAdmin()
            SuccessResponse(token, HttpStatusCode.Created.toString())
        }
        else{
            throw InvalidLoginForAdminException("Imposter", HttpStatusCode.Unauthorized)
        }
    }
    fun tokenGeneratorAdmin(): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.HMAC256(secretAdmin))
    }
}