package com.example.repositories

import com.example.data.model.DeleteSong
import com.example.data.model.InputSong
import com.example.data.model.SuccessResponse
import com.example.data.DatabaseFactory
import com.example.data.schemas.SongsTable
import com.example.utils.helperfunctions.HelperMethods
import com.example.utils.helperfunctions.RowMapping
import com.example.utils.SongAlreadyExistsException
import com.example.utils.SongNotFoundException
import com.example.dao.InterfaceAdmin
import com.example.utils.InvalidLoginForAdminException
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class InterfaceAdminImpl: InterfaceAdmin {
    private val rowMapping= RowMapping()

    override suspend fun checkSong(song: String,artist:String):Boolean{
        return DatabaseFactory.dbQuery {
            SongsTable.selectAll().map { rowMapping.mapSong(it) }.any { it.artist == artist && it.tittle == song }
        }
    }

    override suspend fun addSong(details: InputSong): SuccessResponse {
        return if (!checkSong(details.tittle!!,details.artist!!)) {
                DatabaseFactory.dbQuery {
                    SongsTable.insert {
                        it[title] = details.tittle
                        it[artist] = details.artist
                        it[duration] = details.duration!!
                    }

                }
                SuccessResponse("Song ${details.tittle} Has Added", HttpStatusCode.Accepted.toString())
            } else {
                throw SongAlreadyExistsException(msg="Song ${details.tittle} Already Exists",HttpStatusCode.BadRequest)
            }
    }

    override suspend fun deleteSong(details: DeleteSong): SuccessResponse {
        return if (checkSong(details.tittle!!, details.artist!!)) {
            DatabaseFactory.dbQuery {
                SongsTable.deleteWhere { artist eq details.artist and (title eq details.tittle) }
            }
            SuccessResponse("${details.tittle} Has Deleted ", HttpStatusCode.Accepted.toString())
        } else {
            throw SongNotFoundException("Song ${details.tittle} Does Not Exists", HttpStatusCode.BadRequest)
        }
    }



}


