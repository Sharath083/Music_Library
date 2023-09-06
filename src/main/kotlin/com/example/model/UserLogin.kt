package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class UserLogin(val name:String?,val password:String?)