package com.example.data.model

import kotlinx.serialization.Serializable
@Serializable
data class ExceptionResponse(val response:String,val msgId:Int, val status:String)

