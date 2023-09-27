package com.example.utils.helperfunctions

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.JWTData.audience
import com.example.config.JWTData.issuer
import com.example.config.JWTData.secretAdmin
import org.koin.core.component.KoinComponent
import java.security.SecureRandom
import java.util.*

class HelperMethods: KoinComponent {
    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"))
    }
    fun tokenGeneratorAdmin(): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.HMAC256(secretAdmin))
    }
    fun generateSessionId(length: Int = 32): String {
        val random = SecureRandom()
        val bytes = ByteArray(length)
        random.nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }
}