package com.example.data.repositories

import com.example.data.model.DeleteSong
import com.example.data.model.InputSong
import com.example.data.model.Response
import com.example.data.model.LoginResponse
import com.example.data.DatabaseFactory
import com.example.data.schemas.SongsTable
import com.example.utils.helperfunctions.HelperMethods
import com.example.utils.helperfunctions.RowMapping
import com.example.domain.exception.SongAlreadyExistsException
import com.example.domain.exception.SongNotFoundException
import com.example.domain.interfaces.InterfaceAdmin
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class InterfaceAdminImpl(): InterfaceAdmin {
    private val rowMapping= RowMapping()
    private val methods= HelperMethods()
    override  fun adminLoginCheck(name:String, password:String): LoginResponse {
        return if(name==("admin") && password=="1234"){
            val token=methods.tokenGeneratorAdmin()
                LoginResponse(token,HttpStatusCode.Created.toString())
        }
        else{
            LoginResponse("Imposter",HttpStatusCode.Unauthorized.toString())
        }
    }

    override suspend fun checkSong(song: String,artist:String):Boolean{
        return DatabaseFactory.dbQuery {
            SongsTable.selectAll().map { rowMapping.mapSong(it) }.any { it.artist == artist && it.tittle == song }
        }
    }

    override suspend fun addSong(details: InputSong): Response<String> {
        return  try {
            if (!checkSong(details.tittle!!,details.artist!!)) {
                DatabaseFactory.dbQuery {
                    SongsTable.insert {
                        it[title] = details.tittle
                        it[artist] = details.artist
                        it[duration] = details.duration!!
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

