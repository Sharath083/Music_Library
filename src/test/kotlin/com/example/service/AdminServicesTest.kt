package com.example.service


import com.example.H2Database
import com.example.database.table.Songs
import com.example.di.myModule
import com.example.repositories.AdminInterfaceImpl
import com.example.exceptions.InvalidLoginForAdminException
import com.example.exceptions.SongAlreadyExistsException
import com.example.exceptions.SongNotFoundException
import com.example.model.*
import com.example.utils.helperfunctions.HelperMethods
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.sql.Connection
import kotlin.test.assertEquals


class AdminServicesTest:KoinComponent{
    private lateinit var database: Database
    private val input=InputSong("cold", "coldpaly", "3:47")
    private val deleteSong=DeleteSong("cold", "coldpaly")
    private val adminServices =AdminServices()
    private val helperFunction =HelperMethods()
    @Before
    fun setup() {
        startKoin {
            modules(myModule)
        }
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ
        transaction  (database) {
            SchemaUtils.create(Songs)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        transaction(database) {
            SchemaUtils.drop(Songs)
        }
    }



    @Test
    fun adminLoginServiceTest(): Unit {
        adminServices.adminLoginService("admin","1234").apply {
            assertEquals(helperFunction.tokenGeneratorAdmin(),this.response)
        }
        try {
            adminServices.adminLoginService("abc", "123").apply {
            }
        }catch (e:InvalidLoginForAdminException){
            assertEquals("Imposter",e.msg)
        }
    }
    @Test
    fun songAddServiceService(): Unit = runBlocking{

        adminServices.songAddService(input).apply {
            assertEquals("Song ${input.song} Has Added",this.response)
        }
        try {
            adminServices.songAddService(input)
        }catch (e:SongAlreadyExistsException){
            assertEquals("Song ${input.song} Already Exists",e.msg)
        }
    }
    @Test
    fun deleteSongServiceTest(): Unit= runBlocking {
        adminServices.songAddService(input)
        adminServices.deleteSongService(deleteSong).apply {
            assertEquals("${deleteSong.tittle} Has Deleted",this.response)
        }
        try {
            adminServices.deleteSongService(deleteSong)
        }catch (e:SongNotFoundException){
            assertEquals("Song ${deleteSong.tittle} Does Not Exists",e.msg)
        }
    }

}