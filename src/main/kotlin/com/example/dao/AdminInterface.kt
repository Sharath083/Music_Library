package com.example.dao

import com.example.data.model.DeleteSong
import com.example.data.model.InputSong
import com.example.data.model.SuccessResponse
import java.util.UUID

interface AdminInterface {
    fun adminLoginCheck(name:String, password:String):Boolean
    suspend fun checkSong(song: String,artist:String):Boolean
    suspend fun addSong(details: InputSong): Boolean
    suspend fun getSongId(details: DeleteSong):UUID?
    suspend fun deleteSong(details: DeleteSong)



}
