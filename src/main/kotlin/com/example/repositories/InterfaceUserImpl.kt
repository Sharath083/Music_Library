package com.example.repositories

import com.example.data.DatabaseFactory
import com.example.data.schemas.PlayListTable
import com.example.data.schemas.SongsTable
import com.example.data.schemas.UserTable
import com.example.utils.helperfunctions.RowMapping
import com.example.dao.InterfaceUser
import com.example.data.model.*
import com.example.utils.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.sql.SQLException

class InterfaceUserImpl: InterfaceUser {
    private val rowMapping= RowMapping()
    override fun getAllSongs():List<InputSong>{
            return SongsTable.selectAll().map { rowMapping.mapSong(it) }

    }
    override suspend fun getUserId(name: String): Int {
        return DatabaseFactory.dbQuery {
            UserTable.slice(UserTable.id).select(UserTable.userName eq name)
                .map { rowMapping.mapUserId(it) }.firstOrNull()
        }?:throw UserDoesNotExistsException("USER DOES NOT EXISTS WITH NAME $name",HttpStatusCode.BadRequest)
    }

    override suspend fun userLoginCheck(name:String, password:String): SuccessResponse {
        val user= DatabaseFactory.dbQuery {
            UserTable.select(UserTable.userName eq name and (UserTable.password eq password))
                .map { rowMapping.resultRowRegisteredUser(it) }.firstOrNull()
        }
        return if(name==user?.name && password== user.password){
            SuccessResponse("Login Success",HttpStatusCode.Accepted.toString())
        }
        else{
            throw InvalidUserNameOrPasswordException("Invalid Login details",HttpStatusCode.BadRequest)
        }
    }
    override suspend fun deleteAccount(userId: Int): SuccessResponse {
            val result= DatabaseFactory.dbQuery {
                PlayListTable.deleteWhere { PlayListTable.userId eq userId }
                UserTable.deleteWhere {UserTable.id eq userId }
            }
            return if(result>0){
                SuccessResponse("Account Has Deleted",HttpStatusCode.Accepted.toString())
            }
            else{
                throw UserDoesNotExistsException("Invalid UserName Or Password",HttpStatusCode.BadRequest)
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

    override suspend fun viewPlayList(playList: String, userId: Int): OutputList<List<InputSong>> {
            val list= DatabaseFactory.dbQuery {
                PlayListTable.innerJoin(SongsTable)
                    .select(PlayListTable.playListName eq playList and (PlayListTable.userId eq userId))
                    .map { rowMapping.mapSong(it) }
            }
            return if(list.isNotEmpty()){
                OutputList(list,HttpStatusCode.Accepted.toString())
            }
            else{
                throw PlayListNotFoundException("$playList Does Not Exists",HttpStatusCode.BadRequest)
            }
    }

    override suspend fun userRegistration(details: UserRegistration): SuccessResponse {
            return if(checkUser(details.name!!,details.email!!)) {
                DatabaseFactory.dbQuery {
                    UserTable.insert {
                        it[userName] = details.name
                        it[gmail] = details.email
                        it[password] = details.password!!
                    }
                }
                SuccessResponse("Registered Successfully",HttpStatusCode.Created.toString())
            }
            else{
                throw UserAlreadyExistsException("${details.name} or ${details.email} Already Exists ",HttpStatusCode.BadRequest)
            }
    }
    override suspend fun filterByArtist(artist:String): OutputList<List<InputSong>> {
            val list= DatabaseFactory.dbQuery {
                SongsTable.select(SongsTable.artist eq artist).map {rowMapping.mapSong(it) }.filter { it.artist == artist }
            }
            return if(list.isNotEmpty()){
                OutputList(list,HttpStatusCode.Accepted.toString())
            }
            else{
                throw ArtistDoesNotExistsException("$artist Does Not Exists",HttpStatusCode.BadRequest)
            }

    }

    override suspend fun addToPlayList(details: AddToPlayList,usersId:Int): SuccessResponse {

        return if(!checkSongInDb(details.song!!)){
            throw SongNotFoundException("Song Does Not Exists In DB",HttpStatusCode.BadRequest)
        }
        else if (checkSongInPlayList(details.song,details.playList!!,usersId) ) {
            DatabaseFactory.dbQuery {
                PlayListTable.insert {
                    it[userId]=usersId
                    it[playListName]= details.playList
                    it[songId]= getSongId(details.song)
                }
            }
            SuccessResponse("Song ${details.song} Added To PlayList ${details.playList}",HttpStatusCode.Accepted.toString())
        }
        else{
            throw SongAlreadyExistsException("${details.song } Already  Exists In PlayList ${details.playList}",HttpStatusCode.BadRequest)
        }
    }

    override suspend fun removeFromPlayList(details: RemoveFromPlayList,usersId:Int): SuccessResponse {
            return if (!checkSongInPlayList(details.song!!,details.playList!!,usersId) ) {
                DatabaseFactory.dbQuery {
                    PlayListTable.deleteWhere {
                        songId eq getSongId(details.song) and (userId eq usersId) and(playListName eq details.playList)
                    }
                }
                SuccessResponse("Song ${details.song} Has Removed From PlayList ${details.playList}",HttpStatusCode.Accepted.toString())
            }
            else{
                throw SongNotFoundException("${details.song } Does Not Exists in PlayList",HttpStatusCode.BadRequest)
            }
    }
}