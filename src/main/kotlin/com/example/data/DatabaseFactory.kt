package com.example.data


import com.example.database.table.PlayList
import com.example.database.table.Songs
import com.example.database.table.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object DatabaseFactory {
    fun init(){
        val url = "jdbc:postgresql://localhost:5432/musicLibrary"
        val driver = "org.postgresql.Driver"
        val user = "postgres"
        val password = "root"
        Database.connect(url, driver, user, password)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(PlayList)
            SchemaUtils.createMissingTablesAndColumns(Songs)
            SchemaUtils.createMissingTablesAndColumns(Users)        }
    }




    suspend fun <T> dbQuery(block: () -> T):T=newSuspendedTransaction(Dispatchers.IO){
        block()
    }
}