package com.example.repository

import com.example.H2Database
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
import com.example.entity.PlayListEntity
import com.example.entity.SongsEntity
import com.example.entity.UserEntity
import com.example.exceptions.SongNotFoundException
import com.example.exceptions.UserDoesNotExistsException
import com.example.model.*
import com.example.repositories.AdminInterfaceImpl
import com.example.repositories.UserInterfaceImpl
import com.example.utils.helperfunctions.Mapping
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException
import java.sql.Connection
import java.util.*
import kotlin.test.*

class UserInterfaceImplTest  {
    private lateinit var database: Database

    private val userInterfaceImpl=UserInterfaceImpl()
    private val adminInterfaceImpl= AdminInterfaceImpl()
    private val register=UserRegistration("sharath","sharath@gmail.com","12345")
    private val input=InputSong("cold", "coldpaly", "3:47")
    private val playList=AddToPlayList("cold", "my1")
    private val removePlayList=RemoveFromPlayList("cold", "my1")

    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ
        transaction  (database) {
            SchemaUtils.create(PlayList,Songs,Users)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(PlayList,Songs,Users)
        }
    }
    @Test
    fun userRegistrationTest(): Unit = runBlocking {
        userInterfaceImpl.userRegistration(register).apply {
            assertEquals("sharath",this.name)
        }
    }
    @Test
    fun checkUserTest(): Unit = runBlocking {
        userInterfaceImpl.userRegistration(register)
        userInterfaceImpl.checkUser(register.name!!,register.email!!).apply {
            assertFalse(this)
        }
        userInterfaceImpl.checkUser("abc","abc@gmail.com").apply {
            assertTrue(this)
        }
    }
    @Test
    fun userLoginTest(): Unit = runBlocking {
        userInterfaceImpl.userRegistration(register)
        userInterfaceImpl.userLoginCheck(UserLogin(register.name!!,register.password!!)).apply {
            assertTrue(this)
        }
        userInterfaceImpl.userLoginCheck(UserLogin("abc","123")).apply {
            assertFalse(this)
        }

    }
    @Test
    fun getUserIdTest() : Unit = runBlocking {
        userInterfaceImpl.userRegistration(register)
        userInterfaceImpl.getUserId(register.name!!).apply {
            assertTrue(this.toString().isNotEmpty())
        }
        userInterfaceImpl.getUserId("abc").apply {
            assertEquals(null,this)
        }
    }
    @Test
    fun filterByArtistTest() : Unit = runBlocking{
        adminInterfaceImpl.addSong(input)
        userInterfaceImpl.filterByArtist(input.artist!!).apply {
            assertEquals("cold",this[0].song)
            assertNotEquals("abc",this[0].song)
        }
    }
    @Test
    fun getSongIdTest():Unit= runBlocking {
        adminInterfaceImpl.addSong(input)
        transaction {
            userInterfaceImpl.getSongId(input.song!!).apply {
                assertTrue(this.toString().isNotEmpty())
            }
            userInterfaceImpl.getSongId("abc").apply {
                assertNull(this)
            }
        }
    }
    @Test
    fun checkSongInDbTest():Unit= runBlocking{
        adminInterfaceImpl.addSong(input)
        userInterfaceImpl.checkSongInDb(input.song!!).apply {
            assertTrue(this)
        }
        userInterfaceImpl.checkSongInDb("abc").apply {
            assertFalse(this)
        }
    }
    @Test
    fun addToPlayList():Unit= runBlocking {
        val userId=userInterfaceImpl.userRegistration(register).uuid
        adminInterfaceImpl.addSong(input)
        val uuid=UUID.fromString(userId)
        userInterfaceImpl.addToPlayList(playList,uuid).apply {
            assertTrue(this)
        }
//        userInterfaceImpl.addToPlayList(AddToPlayList("abc","123"),uuid).apply {
//            assertFailsWith<SongNotFoundException> { "Song Not Found Exception" }
//        }

    }
    @Test
    fun checkSongInPlayListTest():Unit= runBlocking {
        val userId=userInterfaceImpl.userRegistration(register).uuid
        adminInterfaceImpl.addSong(input)
        val uuid=UUID.fromString(userId)
        userInterfaceImpl.addToPlayList(playList,uuid)
        userInterfaceImpl.checkSongInPlayList(input.song!!,playList.playList!!,uuid).apply {
            assertFalse(this)
        }
//        userInterfaceImpl.checkSongInPlayList("abc",playList.playList!!,uuid).apply {
//            println(this)
//            assertFalse(this)
//        }
    }
    @Test
    fun removeFromPlayListTest():Unit= runBlocking {
        val userId=userInterfaceImpl.userRegistration(register).uuid
        adminInterfaceImpl.addSong(input)
        val uuid=UUID.fromString(userId)
        userInterfaceImpl.addToPlayList(playList,uuid)
        userInterfaceImpl.removeFromPlayList(removePlayList,uuid).apply {
            assertTrue(this)
        }
    }
    @Test
    fun deletePlayListTest():Unit= runBlocking {
        val userId=userInterfaceImpl.userRegistration(register).uuid
        adminInterfaceImpl.addSong(input)
        val uuid=UUID.fromString(userId)
        userInterfaceImpl.addToPlayList(playList,uuid)
        userInterfaceImpl.deletePlayList(playList.playList!!,uuid).apply {
            assertTrue(this)
        }
    }
    @Test
    fun viewPlayListTest():Unit= runBlocking {
        val userId=userInterfaceImpl.userRegistration(register).uuid
        adminInterfaceImpl.addSong(input)
        val uuid=UUID.fromString(userId)
        userInterfaceImpl.addToPlayList(playList,uuid)
        userInterfaceImpl.viewPlayList(playList.playList!!,uuid).apply {
            assertEquals("cold",this[0].song)
        }
    }
    @Test
    fun deleteAccountTest():Unit= runBlocking {
        val userId=userInterfaceImpl.userRegistration(register).uuid
        adminInterfaceImpl.addSong(input)
        val uuid=UUID.fromString(userId)
        userInterfaceImpl.addToPlayList(playList,uuid)
        userInterfaceImpl.deleteAccount(uuid).apply {
            assertTrue(this)
        }
    }






}