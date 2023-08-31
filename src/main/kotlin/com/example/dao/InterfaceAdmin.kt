package com.example.dao

import com.example.data.model.DeleteSong
import com.example.data.model.InputSong
import com.example.data.model.SuccessResponse

interface InterfaceAdmin {
    fun adminLoginCheck(name:String, password:String): SuccessResponse
    suspend fun checkSong(song: String,artist:String):Boolean
    suspend fun addSong(details: InputSong): SuccessResponse
    suspend fun deleteSong(details: DeleteSong): SuccessResponse
}