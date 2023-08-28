package com.example.data

import com.example.data.schemas.PlayListTable
import com.example.data.schemas.SongsTable
import com.example.data.schemas.UserTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(){
        val url = "jdbc:postgresql://localhost:5432/spotify"
        val driver = "org.postgresql.Driver"
        val user = "postgres"
        val password = "root"
        Database.connect(url, driver, user, password)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(PlayListTable,UserTable,SongsTable)
        }
    }




    suspend fun <T> dbQuery(block:()->T):T=newSuspendedTransaction(Dispatchers.IO){
        block()
    }
}