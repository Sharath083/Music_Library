package com.example.dao

import com.example.model.DeleteSong
import com.example.model.InputSong
import com.example.model.SuccessResponse
import java.util.UUID

interface AdminInterface {
    fun adminLoginCheck(name:String, password:String):Boolean
    suspend fun checkSong(song: String,artist:String):Boolean
    suspend fun addSong(details: InputSong): Boolean
    suspend fun getSongId(details: DeleteSong):UUID?
    suspend fun deleteSong(details: DeleteSong)



}
