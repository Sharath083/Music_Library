package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class UserLogin(val name:String?,val password:String?)