package com.example.domain.interfaces

import com.example.data.model.AddToPlayList
import com.example.data.model.InputSong
import com.example.data.model.RemoveFromPlayList
import com.example.data.model.UserRegistration
import com.example.data.model.LoginResponse
import com.example.data.model.Response

interface InterfaceUser {
    fun getAllSongs():List<InputSong>
    suspend fun getUserId(name: String): Int
    suspend fun userRegistration(details: UserRegistration): Response<String>
    suspend fun userLoginCheck(name:String, password:String): LoginResponse
    suspend fun filterByArtist(artist:String): Response<List<InputSong>>
    suspend fun addToPlayList(details: AddToPlayList,usersId:Int): Response<String>
    suspend fun removeFromPlayList(details: RemoveFromPlayList,usersId:Int): Response<String>
    suspend fun deleteAccount(userId:Int): Response<String>
    suspend fun checkUser(name: String, email: String): Boolean
    suspend fun checkSongInPlayList(song: String,playList:String,usersId:Int):Boolean
    fun getSongId(song: String):Int
    suspend fun checkSongInDb(song: String):Boolean
    suspend fun viewPlayList(playList:String,userId:Int): Response<List<InputSong>>


}