package com.example.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserId(val userId:String):Principal
