package com.example.model
import kotlinx.serialization.Serializable


@Serializable
sealed class BaseResponse {
    @Serializable
    data class SuccessResponse(val response:String, val status:String) : BaseResponse()
    @Serializable
    data class ExceptionResponse(val response:String,val msgId:Int, val status:String): BaseResponse()
}