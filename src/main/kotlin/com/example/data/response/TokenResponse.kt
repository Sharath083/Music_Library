package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(val response:String,val status:String)
