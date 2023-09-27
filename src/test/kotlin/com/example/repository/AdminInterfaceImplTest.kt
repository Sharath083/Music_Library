package com.example.repository
import com.example.H2Database
import com.example.database.table.PlayList
import com.example.database.table.Songs
import com.example.database.table.Users
import com.example.model.DeleteSong
import com.example.model.InputSong
import com.example.repositories.AdminInterfaceImpl
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.UByteArraySerializer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Connection
import kotlin.test.*

class AdminInterfaceImplTest {
    private lateinit var database: Database
//    val adminInterfaceImpl by inject<AdminInterfaceImpl>()
    private val adminInterfaceImpl=AdminInterfaceImpl()
    private val input=InputSong("cold", "coldpaly", "3:47")
    private val song=DeleteSong("cold", "coldpaly")
    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ
        transaction  (database) {
            SchemaUtils.create(Songs)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Songs)
        }
    }

    @Test//Invalid Input
    fun adminLoginTest() :Unit = runBlocking {
        adminInterfaceImpl.adminLoginCheck("ds","3r").apply {
            assertFalse(this)//Invalid Input
        }
        adminInterfaceImpl.adminLoginCheck("admin","1234").apply {
        assertTrue(this)  }//valid Input
    }
    @Test
    fun addSongTest():Unit= runBlocking {
        adminInterfaceImpl.addSong(input).apply {
            assertTrue(this)
        }
    }
    @Test
    fun checkSongTest():Unit= runBlocking {
        adminInterfaceImpl.addSong(input)
        adminInterfaceImpl.checkSong(input.song!!,input.artist!!).apply {
            assertTrue(this)
        }
        adminInterfaceImpl.checkSong("abc","hij").apply {
            assertFalse(this)
        }
    }
    @Test
    fun getSongIdTest():Unit= runBlocking {
        adminInterfaceImpl.addSong(input)
        adminInterfaceImpl.getSongId(song).apply {
            assertTrue (this.toString().isNotEmpty())}
        adminInterfaceImpl.getSongId(DeleteSong("abc","def")).apply {
            assertNull(this)
        }
    }
    @Test
    fun deleteSongTest():Unit= runBlocking {
        adminInterfaceImpl.addSong(input)
        adminInterfaceImpl.deleteSong(song).apply {
            assertTrue(this)
        }
    }
}