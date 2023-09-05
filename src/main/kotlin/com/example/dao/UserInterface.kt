package com.example.dao

import com.example.data.model.*
import java.util.UUID

interface UserInterface {
    suspend fun userRegistration(details: UserRegistration)
    suspend fun checkUser(name: String, email: String): Boolean
    suspend fun userLoginCheck(name:String, password:String): UserCheck?
    suspend fun getUserId(name: String): UUID?

    suspend fun filterByArtist(artist:String):List<InputSong>
    suspend fun checkSongInPlayList(song: String,playList:String,usersId:UUID):Boolean?
    suspend fun checkSongInDb(song: String):Boolean

    suspend fun addToPlayList(details: AddToPlayList, usersId:UUID)
    suspend fun removeFromPlayList(details: RemoveFromPlayList, usersId:UUID): Boolean
    suspend fun deleteAccount(userId:UUID)
    fun getSongId(song: String):UUID
    suspend fun viewPlayList(playList:String,userId:UUID):List<InputSong>

}