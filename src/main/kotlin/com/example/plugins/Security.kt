package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.sessiondata.UserData
import com.example.data.sessiondata.UserDetails
import com.example.domain.exception.InvalidTokenException
import com.example.utils.JWTData
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {
    install(Sessions) {
        val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        cookie<UserData>("user_session") {
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
        cookie<UserDetails>("user_session") {
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }
    val secretUser = JWTData().secretUser
    val secretAdmin = JWTData().secretAdmin
    val issuer = JWTData().issuer
    val audience = JWTData().audience
    authentication {
//        jwt {
//            verifier(
//                JWT
//                    .require(Algorithm.HMAC256(secretUser))
//                    .withAudience(audience)
//                    .withIssuer(issuer)
//                    .build()
//            )
//            validate { credential ->
//                if (credential.payload.audience.contains(audience))
//                    JWTPrincipal(credential.payload)
//                else null
//            }
//            challenge { _, _ ->
//                throw InvalidTokenException("The token is invalid or Expired")
//            }
//        }
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
