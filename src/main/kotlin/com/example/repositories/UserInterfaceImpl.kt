package com.example.repositories

import com.example.dao.UserInterface
import com.example.database.DatabaseFactory
import com.example.database.table.PlayList
import com.example.database.table.PlayList.playListName
import com.example.database.table.PlayList.songId
import com.example.database.table.PlayList.userId
import com.example.database.table.Songs
import com.example.database.table.Users
import com.example.database.table.Users.gmail
import com.example.database.table.Users.password
import com.example.database.table.Users.userName
import com.example.entity.SongsEntity
import com.example.entity.UserEntity
import com.example.exceptions.UserDoesNotExistsException
import com.example.model.*
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
        return DatabaseFactory.dbQuery {
            UserEntity.find { (userName eq name or (gmail eq email)) }
                .toList().isEmpty()
        }
    }

    override suspend fun userLoginCheck(input: UserLogin): Boolean {
        return DatabaseFactory.dbQuery {
            UserEntity.find { (userName eq input.name!! and (password eq input.password!!)) }
                .toList().isNotEmpty()
        }
//        return DatabaseFactory.dbQuery {
//            UserEntity.all()
//                .map { mapping.mapRegisteredUser(it) }.any { it.name==input.name &&it.password==input.password }
//        }

    }

    override suspend fun getUserId(name: String): UUID?{
        return DatabaseFactory.dbQuery {
            UserEntity
                .find { userName eq name }.map { it.id.value }.firstOrNull()
        }
    }

    override suspend fun filterByArtist(artist: String): List<InputSong> {
        return DatabaseFactory.dbQuery {
            SongsEntity.find { Songs.artist eq artist}
                .map {mapping.mapSong(it) }
        }
    }
    override  fun getSongId(song: String): UUID? {
        return SongsEntity.find(Songs.title eq song).toList().firstOrNull()?.id?.value
    }

    override suspend fun checkSongInPlayList(song: String, playList: String, usersId: UUID): Boolean {
//        return DatabaseFactory.dbQuery {
//            PlayListEntity.all().map { mapping.mapPlayListDetails(it) }.any {
//                it.userId.equals(usersId) && it.playListName == playList && it.songId==getSongId(song).toString()
//            }
        return DatabaseFactory.dbQuery {
//            PlayListEntity.find {
//                println("IN PLAYLIST VHRCK $usersId")
//                songId eq sId and (playListName eq playList) and (userId eq usersId)
//            }.toList().isEmpty()
            PlayList.select{songId eq getSongId(song) and (playListName eq playList) and (userId eq usersId)}.toList().isEmpty()
//            PlayListEntity.all().toList().map { it.songId.id.value== sId && (it.playListName == playList) && (it.userId.id.value== usersId) }.isEmpty()

        }
    }

    override suspend fun checkSongInDb(song: String): Boolean {
        return DatabaseFactory.dbQuery {
            SongsEntity.find(Songs.title eq song).toList().isNotEmpty()
        }
    }

    override suspend fun checkPlayList(playList: String, usersId: UUID): Boolean {
        return DatabaseFactory.dbQuery {
            PlayList.select { playListName eq playList and(userId eq usersId) }.toList().isEmpty()
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
    override suspend fun addToPlayList(details: AddToPlayList, usersId: UUID):Boolean{
        val insert= DatabaseFactory.dbQuery {
//            PlayListEntity.new {
//                userId=getUserIds(usersId)
//                playListName= details.playList!!
//                songId= getSongIds(details.song!!)
//            }

            PlayList.insert {
                it[userId]=usersId
                it[playListName]= details.playList!!
                it[songId]= getSongId(details.song!!)!!
            }
        }
        return insert.resultedValues!!.toList().isNotEmpty()


    }

    override suspend fun removeFromPlayList(details: RemoveFromPlayList, usersId: UUID): Boolean {
        return  DatabaseFactory.dbQuery {
                PlayList.deleteWhere{
                    songId eq getSongId(details.song!!)and (userId eq usersId) and (playListName eq details.playList!!)
                }
            }>0
    }

    override suspend fun deletePlayList(playList: String, usersId: UUID): Boolean {
        return DatabaseFactory.dbQuery {
            PlayList.deleteWhere { playListName eq playList and (userId eq usersId) }
        }>0
    }

    override suspend fun viewPlayList(playList: String, userId: UUID): List<InputSong> {
        return DatabaseFactory.dbQuery {
            val query=PlayList.innerJoin(Songs)
                .select{playListName eq playList and (PlayList.userId eq userId)}
            SongsEntity.wrapRows(query).map { mapping.mapSong(it) }
        }
        }
    override suspend fun deleteAccount(userId: UUID) {
        DatabaseFactory.dbQuery {
            println(userId)
            PlayList.deleteWhere {PlayList.userId eq userId }?:throw UserDoesNotExistsException("Invalid UserName Or Password",HttpStatusCode.BadRequest)
            val userData=UserEntity.findById(userId)?:throw UserDoesNotExistsException("Invalid UserName Or Password",HttpStatusCode.BadRequest)
//            playListData.delete()
            userData.delete()

        }
    }
}