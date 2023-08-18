package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val response:String, val status:String)
