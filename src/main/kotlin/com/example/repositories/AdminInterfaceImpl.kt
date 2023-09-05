package com.example.repositories

import com.example.dao.AdminInterface
import com.example.data.DatabaseFactory
import com.example.data.model.DeleteSong
import com.example.data.model.InputSong
import com.example.data.schemas.SongsTable
import com.example.database.table.Songs
import com.example.entity.SongsEntity
import com.example.utils.helperfunctions.Mapping
import com.example.utils.helperfunctions.RowMapping
import io.ktor.http.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class AdminInterfaceImpl : AdminInterface {
    val rowMapping= Mapping()
    override fun adminLoginCheck(name: String, password: String): Boolean {
        return name==("admin") && password=="1234"
    }

    override suspend fun checkSong(song: String, artist: String): Boolean {
        return DatabaseFactory.dbQuery {
            SongsEntity.find { Songs.artist eq artist and (Songs.title eq song) }
                .toList()
                .isNotEmpty()
        }
    }

    override suspend fun addSong(details: InputSong): Boolean {
        return if (!checkSong(details.tittle!!,details.artist!!)) {
            DatabaseFactory.dbQuery {
                SongsEntity.new {
                    title = details.tittle
                    artist = details.artist
                    duration= details.duration!!
                }
            }
            true
        } else {
            false
        }
    }

    override suspend fun deleteSong(details: DeleteSong): Boolean {
        return DatabaseFactory.dbQuery {
                SongsTable.deleteWhere { artist eq details.artist!! and (title eq details.tittle!!) }
            } > 0
    }
}