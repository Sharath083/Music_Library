package com.example.domain.controller

import com.example.data.request.AddToPlayList
import com.example.data.request.InputSong
import com.example.data.request.RemoveFromPlayList
import com.example.data.request.UserRegistration
import com.example.data.response.LoginResponse
import com.example.data.response.Response
import com.example.database.DatabaseFactory
import com.example.database.table.PlayListTable
import com.example.database.table.SongsTable
import com.example.database.table.UserTable
import com.example.utils.Methods
import com.example.utils.RowMapping
import com.example.domain.exception.*
import com.example.domain.interfaces.InterfaceUser
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.sql.SQLException

class InterfaceUserImpl: InterfaceUser {
    private val rowMapping= RowMapping()
    private val methods= Methods()

    override fun getAllSongs():List<InputSong>{
            return SongsTable.selectAll().map { rowMapping.mapSong(it) }

    }
    override suspend fun getUserId(name: String): Int {
        return DatabaseFactory.dbQuery {
            UserTable.slice(UserTable.id).select(UserTable.userName eq name)
                .map { rowMapping.mapUserId(it) }.firstOrNull()
        }?:throw UserDoesNotExistsException("USER DOES NOT EXISTS WITH NAME $name")
    }

    override suspend fun userLoginCheck(name:String, password:String): LoginResponse {
        val user=DatabaseFactory.dbQuery {
            UserTable.select(UserTable.userName eq name and (UserTable.password eq password))
                .map { rowMapping.resultRowRegisteredUser(it) }.firstOrNull()
        }
        return if(name==user?.name && password== user.password){
            LoginResponse("Login Success",HttpStatusCode.Accepted.toString())
        }
        else{
            throw InvalidUserNameOrPasswordException("Invalid Login details")
        }
    }
    override suspend fun deleteAccount(name:String, password:String): Response<String> {
        return try {
            val userId=getUserId(name)
            val result=DatabaseFactory.dbQuery {
                PlayListTable.deleteWhere { PlayListTable.userId eq userId }
                UserTable.deleteWhere { (userName eq name and (UserTable.password eq password))}
            }
            return if(result>0){
                Response.Success("Account Has Deleted",HttpStatusCode.Accepted.toString())
            }
            else{
                throw UserDoesNotExistsException("Invalid UserName Or Password")
            }
        }catch (e:Exception){
            when(e){
                is UserDoesNotExistsException->Response.Error("$e ${e.msg}",HttpStatusCode.BadRequest.toString())
                is SQLException->Response.Error("$e ",HttpStatusCode.BadRequest.toString())
                else->{
                    Response.Error("$e System Error", HttpStatusCode.BadRequest.toString())
                }
            }
        }

    }
    override suspend fun checkUser(name: String, email: String): Boolean {
        val data = DatabaseFactory.dbQuery {
            UserTable.select(UserTable.userName eq name or (UserTable.gmail eq email))
                .map { rowMapping.resultRowRegisteredUser(it) }
        }
        return data.isEmpty()
    }
    override suspend fun checkSongInPlayList(song: String,playList:String,usersId:Int):Boolean{
        return DatabaseFactory.dbQuery {
            PlayListTable.select(PlayListTable.songId eq getSongId(song) and(PlayListTable.playListName eq playList) and(PlayListTable.userId eq usersId)).map { rowMapping.mapPlayListDetails(it) }.isEmpty()
        }

    }
    override fun getSongId(song: String):Int{
        return SongsTable.select(SongsTable.title eq song).map { rowMapping.mapSongData(it) }.first().songId
    }
    override suspend fun checkSongInDb(song: String):Boolean{
        return DatabaseFactory.dbQuery {
            SongsTable.selectAll().map { rowMapping.mapSong(it) }.any { it.tittle==song }
        }
    }

    override suspend fun viewPlayList(playList: String, userId: Int): Response<List<InputSong>>{
        return try {
            val list=DatabaseFactory.dbQuery {
                PlayListTable.innerJoin(SongsTable)
                    .select(PlayListTable.playListName eq playList and (PlayListTable.userId eq userId))
                    .map { rowMapping.mapSong(it) }

            }
            if(list.isNotEmpty()){
                Response.OutputList(list,HttpStatusCode.Accepted.toString())
            }
            else{
                throw PlayListNotFoundException("$playList Does Not Exists")
            }

        }catch (e:Exception){
            when(e){
                is PlayListNotFoundException->Response.Error("$e ${e.msg}",HttpStatusCode.BadRequest.toString())
                is SQLException->Response.Error("$e ",HttpStatusCode.BadRequest.toString())
                else->{
                    Response.Error("$e System Error", HttpStatusCode.BadRequest.toString())
                }
            }

        }
    }

    override suspend fun userRegistration(details: UserRegistration):Response<String>{
        return try {
            if(checkUser(details.name!!,details.email!!)) {
                DatabaseFactory.dbQuery {
                    UserTable.insert {
                        it[userName] = details.name
                        it[gmail] = details.email
                        it[password] = details.password!!
                    }
                }
                Response.Success("Registered Successfully",HttpStatusCode.Accepted.toString())
            }
            else{
                throw UserAlreadyExistsException("${details.name} or ${details.email} Already Exists ")
            }
        }catch (e:Exception){
            when(e){
                is UserAlreadyExistsException -> Response.Error("$e ${e.msg}",HttpStatusCode.BadRequest.toString())
                else->{
                    Response.Error("$e System Error", HttpStatusCode.BadRequest.toString())
                }
            }
        }
    }
    override suspend fun filterByArtist(artist:String): Response<List<InputSong>> {
        return try {
            val list=DatabaseFactory.dbQuery {
                SongsTable.select(SongsTable.artist eq artist).map {rowMapping.mapSong(it) }.filter { it.artist == artist }
            }
            if(list.isNotEmpty()){
                Response.OutputList(list,HttpStatusCode.Accepted.toString())
            }
            else{
                throw ArtistDoesNotExistsException("$artist Does Not Exists")
            }

        }catch (e:Exception){
            when(e){
                is ArtistDoesNotExistsException -> Response.Error("$e ${e.msg}",HttpStatusCode.BadRequest.toString())
                else -> {
                    Response.Error("$e",HttpStatusCode.BadRequest.toString())
                }
            }
        }

    }

    override suspend fun addToPlayList(details: AddToPlayList,usersId:Int): Response<String> {
        return try {
            if(!checkSongInDb(details.song!!)){
                throw SongNotFoundException("Song Does Not Exists In DB")
            }
            else if (checkSongInPlayList(details.song,details.playList!!,usersId) ) {
                DatabaseFactory.dbQuery {
                    PlayListTable.insert {
                        it[userId]=usersId
                        it[playListName]= details.playList
                        it[songId]= getSongId(details.song)
                    }
                }
                Response.Success("Song ${details.song} Added To PlayList ${details.playList}",HttpStatusCode.Accepted.toString())
            }
            else{
                throw SongAlreadyExistsException("${details.song } Already  Exists In PlayList ${details.playList}")
            }
        }catch (e:Exception){
            when(e){
                is SongNotFoundException ->Response.Error("$e ${e.msg}",HttpStatusCode.BadRequest.toString())
                is SongAlreadyExistsException ->Response.Error("$e ${e.msg}",HttpStatusCode.BadRequest.toString())
                else -> {
                    Response.Error("$e",HttpStatusCode.BadRequest.toString())
                }
            }
        }

    }

    override suspend fun removeFromPlayList(details: RemoveFromPlayList,usersId:Int): Response<String> {
        return try {
            if (!checkSongInPlayList(details.song!!,details.playList!!,usersId) ) {
                DatabaseFactory.dbQuery {
                    PlayListTable.deleteWhere {
                        songId eq getSongId(details.song) and (userId eq usersId) and(playListName eq details.playList)
                    }
                }
                Response.Success("Song ${details.song} Has Removed From PlayList ${details.playList}",HttpStatusCode.Accepted.toString())
            }
            else{
                throw SongNotFoundException("${details.song } Does Not Exists in PlayList")
            }

        }catch (e:Exception){
            when(e){
                is SongNotFoundException ->Response.Error("$e ${e.msg}",HttpStatusCode.BadRequest.toString())
                is NoSuchElementException->Response.Error("SongNotFoundException",HttpStatusCode.BadRequest.toString())
                else -> {
                    Response.Error("$e",HttpStatusCode.BadRequest.toString())
                }

            }
        }


    }
}