package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
sealed class Response<out T>() {
    @Serializable
    data class OutputList<T>(val data:T,val status: String):Response<T>()
    @Serializable
    data class Success(val response:String,val status:String):Response<Nothing>()
    @Serializable
    data class Error(val response:String,val status:String):Response<Nothing>()
}