package com.example.service
import com.example.H2Database
import com.example.database.table.PlayList
import com.example.database.table.Songs
import com.example.database.table.Users
import com.example.di.myModule
import com.example.exceptions.*
import com.example.model.*
import com.example.repositories.AdminInterfaceImpl

import com.example.repositories.UserInterfaceImpl
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withTimeout
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.sql.Connection
import java.util.*
import kotlin.test.assertEquals

class UserServicesTest  {
    private lateinit var database: Database
    private val userServices=UserServices()
    private val userInterfaceImpl= UserInterfaceImpl()
    private val adminInterfaceImpl=AdminInterfaceImpl()
    private val register=UserRegistration("sharath","sharath@gmail.com","12345")
    private val userLogin=UserLogin("sharath","12345")
    private val input=InputSong("cold", "coldplay", "3:47")
    private val playList=AddToPlayList("cold", "my1")
    private val removePlayList=RemoveFromPlayList("cold", "my1")

    @Before
    fun setup() {
        startKoin {
            modules(myModule)
        }
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ
        transaction  (database) {
            SchemaUtils.create(PlayList, Songs, Users)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        transaction(database) {
            SchemaUtils.drop(PlayList, Songs, Users)
        }
    }

    @Test
    fun userRegistrationServiceTest(): Unit= runBlocking{
        userServices.userRegistrationService(register).apply {
            assertEquals("Registered Successfully",this.response)
        }
        try {
            userServices.userRegistrationService(register)
        }catch (e:UserAlreadyExistsException){
            println(e.localizedMessage)
            assertEquals("${register.name} or ${register.email} Already Exists ",e.msg)
        }
    }

    @Test
    fun userLoginCheckServiceTest(): Unit= runBlocking {
        try {
            userServices.userLoginCheckService(userLogin)
        }catch (e:InvalidUserNameOrPasswordException){
            assertEquals("Invalid Login details",e.msg)
        }
        userServices.userRegistrationService(register)
        userServices.userLoginCheckService(userLogin).apply {
            assertEquals("Login Success",this.response)
        }
    }

    @Test
    fun getUserIdServiceTest():Unit = runBlocking {
        try {
            userServices.getUserIdService(register.name!!)
        }catch (e:UserDoesNotExistsException){
            assertEquals("USER DOES NOT EXISTS WITH NAME ${register.name}",e.msg)
        }
        val uuid=userInterfaceImpl.userRegistration(register).uuid
        userServices.getUserIdService(register.name!!).apply {
            assertEquals(uuid,this.toString())
        }
    }
    @Test
    fun filterByArtistServiceTest(): Unit= runBlocking {
        try {
            userServices.filterByArtistService(input.artist!!)
        }catch (e:ArtistDoesNotExistsException){
            assertEquals("${input.artist} Does Not Exists",e.msg)
        }
        adminInterfaceImpl.addSong(input)
        userServices.filterByArtistService(input.artist!!).apply {
            assertEquals("coldplay",this.data[0].artist)
        }
    }

    @Test
    fun addSongToPlayListServiceTest(): Unit= runBlocking {
        val user= userInterfaceImpl.userRegistration(register).uuid
        val uuid= UUID.fromString(user)
        try {
            userServices.addSongToPlayListService(playList,uuid)
        }catch (e:SongNotFoundException){
            assertEquals("Song Does Not Exists In DB",e.msg)
        }
        adminInterfaceImpl.addSong(input)
        userServices.addSongToPlayListService(playList,uuid).apply {
            println(this)
            assertEquals("Song ${playList.song} Added To PlayList ${playList.playList}",
                this.response)
        }
        adminInterfaceImpl.addSong(input)
        try {
            userServices.addSongToPlayListService(playList,uuid)
        }catch (e:SongAlreadyExistsException){
            assertEquals("${playList.song} Already  Exists In PlayList ${playList.playList}",
                e.msg)
        }
    }

    @Test
    fun removeFromPlayListServiceTest(): Unit= runBlocking {
        val user= userInterfaceImpl.userRegistration(register).uuid
        val uuid= UUID.fromString(user)
        adminInterfaceImpl.addSong(input)//song added to db
        userInterfaceImpl.addToPlayList(playList,uuid)//song added to playlist
        userServices.removeFromPlayListService(removePlayList, uuid).apply {
            assertEquals("Song ${removePlayList.song} Has Removed From PlayList ${removePlayList.playList}"
                ,this.response)
        }
//        val removePlayList=RemoveFromPlayList("cold", "1")
////        userInterfaceImpl.addToPlayList(playList,uuid)//song added to playlist
//        try {//db check
//
//            userServices.removeFromPlayListService(removePlayList, uuid)
//            println(uuid)
//        }catch (e:Exception){
//            when(e) {
//                is SongNotFoundException-> {assertEquals("${removePlayList.song} Does Not Exists in PlayList", e.msg)
//                e.message}
//                is PlayListNotFoundException ->{assertEquals("my1 Does Not Exists",e.msg)
//                println(e.message)}
//
//            }
//        }
//        try {//playlist Check
//            userServices.removeFromPlayListService(RemoveFromPlayList("cold", "my1"), uuid)//invalid playlist name
//        }catch (e:PlayListNotFoundException){
//            assertEquals("my1 Does Not Exists",e.msg)
//        }



//        try {//song  check in playlist
//            userServices.removeFromPlayListService(removePlayList, uuid)
//        }catch (e:SongNotFoundException){
//            assertEquals("${removePlayList.song} Does Not Exists in PlayList",e.msg)
//        }

    }

    @Test
    fun deletePlayListServiceTest(): Unit = runBlocking {
        val user= userInterfaceImpl.userRegistration(register).uuid
        val uuid= UUID.fromString(user)
        adminInterfaceImpl.addSong(input)
        try {
            userServices.deletePlayListService("1",uuid)
        }catch (e:PlayListNotFoundException){
            assertEquals("playList Does Not Exists",e.msg)
        }
        userInterfaceImpl.addToPlayList(playList,uuid)
        userServices.deletePlayListService(playList.playList!!,uuid).apply {
            assertEquals("${playList.playList} Is Deleted",
                this.response)
        }
    }

    @Test
    fun viewPlayListServiceTest():Unit= runBlocking {
        val user= userInterfaceImpl.userRegistration(register).uuid
        val uuid= UUID.fromString(user)
        adminInterfaceImpl.addSong(input)
        userInterfaceImpl.addToPlayList(playList,uuid)
        userServices.viewPlayListService(playList.playList!!,uuid).apply {
            assertEquals("cold",this.data[0].song)
        }
        try {
            userServices.viewPlayListService("1",uuid)
        }catch (e:PlayListNotFoundException){
            assertEquals("playList Does Not Exists",e.msg)
        }
    }
    @Test
    fun deleteAccountServiceTest(): Unit= runBlocking {
        val user= userInterfaceImpl.userRegistration(register).uuid
        val uuid= UUID.fromString(user)
        userServices.deleteAccountService(uuid).apply {
            println(this)
            assertEquals("Account Has Deleted",this.response)
        }
        try {
            userServices.deleteAccountService(uuid)
        }catch (e:UserDoesNotExistsException){
            assertEquals("User Does Not Exists",e.msg)
        }
    }
}
