package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val response:String, val status:String)
