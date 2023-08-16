package com.example.database

import com.example.database.table.PlayListTable
import com.example.database.table.SongsTable
import com.example.database.table.UserTable
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
            SchemaUtils.createMissingTablesAndColumns(PlayListTable)
            SchemaUtils.createMissingTablesAndColumns(UserTable)
            SchemaUtils.createMissingTablesAndColumns(SongsTable)
        }

    }




    suspend fun <T> dbQuery(block:()->T):T=newSuspendedTransaction(Dispatchers.IO){
        block()
    }
}