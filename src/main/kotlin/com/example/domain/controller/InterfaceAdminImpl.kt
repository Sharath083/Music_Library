package com.example.domain.controller

import com.example.data.request.DeleteSong
import com.example.data.request.InputSong
import com.example.data.response.Response
import com.example.data.response.TokenResponse
import com.example.database.DatabaseFactory
import com.example.database.table.SongsTable
import com.example.utils.Methods
import com.example.utils.RowMapping
import com.example.domain.exception.SongAlreadyExistsException
import com.example.domain.exception.SongNotFoundException
import com.example.domain.interfaces.InterfaceAdmin
import io.ktor.http.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class InterfaceAdminImpl(): InterfaceAdmin {
    private val rowMapping= RowMapping()
    private val methods= Methods()
    override  fun adminLoginCheck(name:String, password:String):TokenResponse{
        return if(name==("admin") && password=="1234"){
            val token=methods.tokenGeneratorAdmin()
                TokenResponse(token,HttpStatusCode.Created.toString())
        }
        else{
            TokenResponse("Imposter",HttpStatusCode.Unauthorized.toString())
        }
    }

    override suspend fun checkSong(song: String,artist:String):Boolean{
        return DatabaseFactory.dbQuery {
            SongsTable.selectAll().map { rowMapping.mapSong(it) }.any { it.artist == artist && it.tittle == song }
        }
    }

    override suspend fun addSong(details: InputSong):Response<String>{
        return  try {
            if (!checkSong(details.tittle!!,details.artist)) {
                DatabaseFactory.dbQuery {
                    SongsTable.insert {
                        it[title] = details.tittle
                        it[artist] = details.artist
                        it[duration] = details.duration
                    }

                }
                Response.Success("Song ${details.tittle} Has Added", HttpStatusCode.Accepted.toString())
            } else {
                throw SongAlreadyExistsException("Song ${details.tittle} Already Exists")
            }
        }catch (e:Exception){
            when(e){
                is SongAlreadyExistsException -> Response.Error("${e.msg} $e",HttpStatusCode.BadRequest.toString())
                else -> {
                    Response.Error("$e",HttpStatusCode.BadRequest.toString())
                }
            }
        }

    }

    override suspend fun deleteSong(details: DeleteSong): Response<String> {
        return try {
            if(checkSong(details.tittle!!,details.artist!!)){
                DatabaseFactory.dbQuery {
                    SongsTable.deleteWhere { artist eq details.artist and (title eq details.tittle) }
                }
                Response.Success("${details.tittle} Has Deleted ",HttpStatusCode.Accepted.toString())
            }
            else{
                throw SongNotFoundException("Song ${details.tittle} Does Not Exists")
            }
        }catch (e:Exception){
            when(e){
                is SongNotFoundException -> Response.Error("${e.msg} $e",HttpStatusCode.BadRequest.toString())
                else -> {
                    Response.Error("$e",HttpStatusCode.BadRequest.toString())
                }
            }
        }

    }


}