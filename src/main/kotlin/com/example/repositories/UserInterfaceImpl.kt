package com.example.repositories

import com.example.dao.UserInterface
import com.example.data.DatabaseFactory
import com.example.data.model.*
import com.example.data.schemas.PlayListTable
import com.example.data.schemas.SongsTable
import com.example.data.schemas.UserTable
import com.example.database.table.PlayList
import com.example.database.table.PlayList.playListName
import com.example.database.table.PlayList.songId
import com.example.database.table.PlayList.userId
import com.example.database.table.Songs
import com.example.database.table.Users
import com.example.database.table.Users.gmail
import com.example.database.table.Users.password
import com.example.database.table.Users.userName
import com.example.entity.PlayListEntity
import com.example.entity.SongsEntity
import com.example.entity.UserEntity
import com.example.utils.*
import com.example.utils.helperfunctions.Mapping
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class UserInterfaceImpl : UserInterface {
    private val mapping=Mapping()
    override suspend fun userRegistration(details: UserRegistration) {
        DatabaseFactory.dbQuery {
            UserEntity.new {
                userName = details.name!!
                gmail= details.email!!
                password = details.password!!
            }
        }
    }
    override suspend fun checkUser(name: String, email: String): Boolean {
        val data = DatabaseFactory.dbQuery {
            UserEntity.find { (userName eq name or (gmail eq email))}
                .map { mapping.resultRowRegisteredUser(it) }
        }
        return data.isEmpty()
    }

    override suspend fun userLoginCheck(name: String, password: String): UserCheck? {
        return DatabaseFactory.dbQuery {
            UserEntity.find { userName eq name and (Users.password eq password)}
                .map { mapping.resultRowRegisteredUser(it) }.firstOrNull()
        }
    }

    override suspend fun getUserId(name: String): UUID?{
        return UserEntity
                .find { userName eq name}
                .map { it.id.value }.firstOrNull()

    }

    override suspend fun filterByArtist(artist: String): List<InputSong> {
        return DatabaseFactory.dbQuery {
            SongsEntity.find { Songs.artist eq artist}
                .map {mapping.mapSong(it) }
        }
    }
    override fun getSongId(song: String): UUID {
        return SongsEntity.find(Songs.title eq song).map { it.id.value }.first()
    }
    override suspend fun checkSongInPlayList(song: String, playList: String, usersId: UUID): Boolean {
        DatabaseFactory.dbQuery {
//            PlayListEntity.all().map { mapping.mapPlayListDetails(it) }.filter {
//                it.songId==getSongIds(song) && it.playListName==playList && it.userId == usersId
//            }
            PlayListEntity.find {
                songId eq getSongId(song) and(playListName eq playList) and(userId eq usersId)}
                .map { mapping.mapPlayListDetails(it) }

        }.apply {
            println(this)
            return when {
                this.isEmpty() -> true
                else -> {
                    throw SongAlreadyExistsException(
                        "$song Already  Exists In PlayList $playList", HttpStatusCode.BadRequest)
                }
            }
        }
    }

    override suspend fun checkSongInDb(song: String): Boolean {

        DatabaseFactory.dbQuery {
            SongsEntity.find(Songs.title eq song).map { mapping.mapSong(it) }


        }.apply {
            return if(this.isNotEmpty()){
                println("true db check")
                true

            }else{
                println("exception db check")
                throw SongNotFoundException("Song $song Does Not Exists In DB",HttpStatusCode.BadRequest)
            }
        }
    }
    fun getSongIds(song: String): SongsEntity {
        return SongsEntity.find(Songs.title eq song).map { it }.first()
    }
    fun getUserIds(id: UUID):UserEntity {
            return UserEntity
                .find(Users.id eq id)
                .map { it}.first()

    }
    override suspend fun addToPlayList(details: AddToPlayList, usersId: UUID) {
        DatabaseFactory.dbQuery {
//            PlayListEntity.new {
//                userId=getUserIds(usersId)
//                playListName= details.playList!!
//                songId= getSongIds(details.song!!)
//            }
            PlayList.insert {
                it[userId]=usersId
                it[playListName]= details.playList!!
                it[songId]= getSongId(details.song!!)
            }
        }
    }

    override suspend fun removeFromPlayList(details: RemoveFromPlayList, usersId: UUID): Boolean {
            return  DatabaseFactory.dbQuery {
                PlayList.deleteWhere{
                    songId eq getSongId(details.song!!) and (userId eq usersId) and (playListName eq details.playList!!)
                }
            }>0
    }
    override suspend fun viewPlayList(playListName: String, userId: UUID): List<InputSong> {
        DatabaseFactory.dbQuery {
            PlayList.innerJoin(Songs)
                .select(PlayList.playListName eq playListName and (PlayList.userId eq userId))
        }.apply {
            return SongsEntity.wrapRows(this).map { mapping.mapSong(it) }
        }

        }
    override suspend fun deleteAccount(userId: UUID) {
        DatabaseFactory.dbQuery {
            val playListData=PlayListEntity.findById(userId)?:throw UserDoesNotExistsException("Invalid UserName Or Password",HttpStatusCode.BadRequest)
            val userData=UserEntity.findById(userId)?:throw UserDoesNotExistsException("Invalid UserName Or Password",HttpStatusCode.BadRequest)
            playListData.delete()
            userData.delete()

        }
    }
}