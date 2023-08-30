package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.model.UserId
import com.example.utils.InvalidTokenException
import com.example.utils.helperfunctions.JWTData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {
    install(Sessions) {
        val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        cookie<UserId>("USER_ID") {
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }

    }
    val secretAdmin = JWTData.secretAdmin
    val issuer = JWTData.issuer
    val audience = JWTData.audience
    authentication {
        jwt("Admin"){
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secretAdmin))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ ->
                throw InvalidTokenException("The token is invalid or Expired", HttpStatusCode.BadRequest)
            }
        }
    }
}
