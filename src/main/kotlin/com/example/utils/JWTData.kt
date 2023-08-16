package com.example.utils

class JWTData{
    val secretUser = "login user"
    val secretAdmin = "admin login"
    val issuer = "http://0.0.0.0:8080/"
    val audience = "http://0.0.0.0:8080/hello"
    val realm = "Unauthorized"


//    val secretUser = environment.config.property("jwt.secretUser").getString()
//    val secretAdmin = environment.config.property("jwt.secretAdmin").getString()
//    val issuer = environment.config.property("jwt.issuer").getString()
//    val audience = environment.config.property("jwt.audience").getString()
//    val realm = "Unauthorized"


}