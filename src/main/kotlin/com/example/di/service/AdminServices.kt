package com.example.di.service

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

    fun tokenGeneratorAdmin(): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.HMAC256(secretAdmin))
    }
}