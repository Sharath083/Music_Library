package com.example.utils.helperfunctions

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class HelperMethods() {
//    private val secretUser = JWTData().secretUser
    private val secretAdmin = JWTData.secretAdmin
    private val issuer = JWTData.issuer
    private val audience = JWTData.audience
    fun tokenGeneratorAdmin():String{
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.HMAC256(secretAdmin))
    }
//    fun tokenGeneratorUser(name:String):String{
//        return JWT.create()
//            .withAudience(audience)
//            .withIssuer(issuer)
//            .withClaim("user",name)
//            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
//            .sign(Algorithm.HMAC256(secretUser))
//    }
    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"))
    }
}