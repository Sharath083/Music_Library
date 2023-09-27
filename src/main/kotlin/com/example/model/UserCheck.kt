package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class UserCheck(val uuid:String,val name:String,val email:String,val password:String)