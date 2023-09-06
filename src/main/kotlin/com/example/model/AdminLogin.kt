package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminLogin(val name:String?,val password:String?)
