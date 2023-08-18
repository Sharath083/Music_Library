package com.example.domain.interfaces

import com.example.data.request.DeleteSong
import com.example.data.request.InputSong
import com.example.data.response.Response
import com.example.data.response.LoginResponse

interface InterfaceAdmin {
    suspend fun checkSong(song: String,artist:String):Boolean
    fun adminLoginCheck(name:String,password:String): LoginResponse
    suspend fun addSong(details: InputSong): Response<String>
    suspend fun deleteSong(details: DeleteSong): Response<String>
}