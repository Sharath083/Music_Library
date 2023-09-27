package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.exceptions.InvalidTokenException
import com.example.config.JWTData
import com.example.model.UserId
import com.example.redis.RedisSessionStorage
import com.example.utils.appconstant.SearchConstants.SESSION_AUTH
import com.example.utils.appconstant.SearchConstants.SESSION_NAME
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import org.koin.ktor.ext.inject
fun Application.configureSecurity(){

    val redisSessionStorage by inject<RedisSessionStorage>()
    install(Sessions) {
        val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        header<UserId>(SESSION_NAME, redisSessionStorage){
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }
    install(Authentication){
        session<UserId>(SESSION_AUTH) {
            validate { session ->
                if(session.userId.isNotEmpty()) session
                else null
            }
            challenge{
                throw InvalidTokenException(
                    "INVALID TOKEN",
                    HttpStatusCode.Unauthorized
                )
            }
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
                if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload)
                else null
            }
            challenge { _, _ ->
                throw InvalidTokenException("The token is invalid or Expired", HttpStatusCode.BadRequest)
            }
        }
    }
}
