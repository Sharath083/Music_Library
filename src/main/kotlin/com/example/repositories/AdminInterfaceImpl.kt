package com.example.repositories

import com.example.dao.AdminInterface
import com.example.database.DatabaseFactory
import com.example.model.DeleteSong
import com.example.model.InputSong
import com.example.database.table.Songs
import com.example.entity.SongsEntity
import com.example.exceptions.SongNotFoundException
import com.example.utils.helperfunctions.HelperMethods
import com.example.utils.helperfunctions.Mapping
import io.ktor.http.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.koin.core.component.KoinComponent
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import java.util.UUID

class AdminInterfaceImpl : AdminInterface {
    override fun adminLoginCheck(name: String, password: String): Boolean {
        return name == "admin" && password == "1234"
    }

    override suspend fun checkSong(song: String, artist: String): Boolean {
        return DatabaseFactory.dbQuery {
            SongsEntity.find { Songs.artist eq artist and (Songs.title eq song) }
                .toList()
                .isNotEmpty()
        }
    }

    override suspend fun addSong(details: InputSong): Boolean {
        DatabaseFactory.dbQuery {
            SongsEntity.new {
                title = details.song!!
                artist = details.artist!!
                duration = details.duration!!
            }
        }.apply {
            return this.artist.isNotEmpty()
        }
    }

    override suspend fun getSongId(details: DeleteSong):UUID? {
        return DatabaseFactory.dbQuery {
            SongsEntity.find { Songs.artist eq details.artist!! and (Songs.title eq details.tittle!!) }
                .map { it }.firstOrNull()?.id?.value
        }
    }


    override suspend fun deleteSong(details: DeleteSong):Boolean {
        val songId=getSongId(details)
        return DatabaseFactory.dbQuery {
            Songs.deleteWhere{id eq songId}
            }>0
    }
}