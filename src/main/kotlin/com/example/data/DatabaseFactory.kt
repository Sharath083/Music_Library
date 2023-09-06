package com.example.data


import com.example.config.DBData
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
        val url = DBData.url
        val driver = DBData.driver
        val user = DBData.user
        val password = DBData.password
        Database.connect(url, driver, user, password)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(PlayList,Songs,Users)
        }
    }

    suspend fun <T> dbQuery(block: () -> T):T= newSuspendedTransaction(Dispatchers.IO){
        block()
    }
}