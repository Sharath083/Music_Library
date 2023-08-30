package com.example.dao

import com.example.data.model.*

interface InterfaceUser {
    fun getAllSongs():List<InputSong>
    suspend fun getUserId(name: String): Int
    suspend fun userRegistration(details: UserRegistration): SuccessResponse
    suspend fun userLoginCheck(name:String, password:String): SuccessResponse
    suspend fun filterByArtist(artist:String): OutputList<List<InputSong>>
    suspend fun addToPlayList(details: AddToPlayList,usersId:Int): SuccessResponse
    suspend fun removeFromPlayList(details: RemoveFromPlayList,usersId:Int): SuccessResponse
    suspend fun deleteAccount(userId:Int):SuccessResponse
    suspend fun checkUser(name: String, email: String): Boolean
    suspend fun checkSongInPlayList(song: String,playList:String,usersId:Int):Boolean
    fun getSongId(song: String):Int
    suspend fun checkSongInDb(song: String):Boolean
    suspend fun viewPlayList(playList:String,userId:Int): OutputList<List<InputSong>>


}