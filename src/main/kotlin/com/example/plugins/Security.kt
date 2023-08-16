package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.sessiondata.UserData
import com.example.data.sessiondata.UserDetails
import com.example.utils.JWTData
import com.example.domain.exception.InvalidTokenException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*

fun Application.configureSecurity() {

    install(Sessions) {
        cookie<UserData>("USER_DATA") {
        }
        cookie<UserDetails>("USER_DETAILS") {
        }
    }
    val secretUser = JWTData().secretUser
    val secretAdmin = JWTData().secretAdmin
    val issuer = JWTData().issuer
    val audience = JWTData().audience
    authentication {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secretUser))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(audience))
                    JWTPrincipal(credential.payload)
                else null
            }
            challenge { _, _ ->
                throw InvalidTokenException("The token is invalid or Expired")
            }
        }
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
                throw InvalidTokenException("The token is invalid or Expired")
            }
        }
    }
}
