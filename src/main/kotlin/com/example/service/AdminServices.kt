package com.example.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.DeleteSong
import com.example.model.InputSong
import com.example.repositories.AdminInterfaceImpl
import com.example.exceptions.InvalidLoginForAdminException
import com.example.exceptions.SongAlreadyExistsException
import com.example.exceptions.SongNotFoundException
import com.example.config.JWTData
import com.example.model.BaseResponse
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class AdminServices: KoinComponent {
    private val adminInterfaceImpl by inject<AdminInterfaceImpl>()
    private val secretAdmin = JWTData.secretAdmin
    private val issuer = JWTData.issuer
    private val audience = JWTData.audience

    fun tokenGeneratorAdmin(): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.HMAC256(secretAdmin))
    }

    fun adminLoginService(name: String, password: String): BaseResponse {
        return if(adminInterfaceImpl.adminLoginCheck(name,password)){
            BaseResponse.SuccessResponse(tokenGeneratorAdmin(), HttpStatusCode.Created.toString())
        }
        else{
            throw InvalidLoginForAdminException("Imposter", HttpStatusCode.Unauthorized)
        }
    }
    suspend fun songAddService(details: InputSong): BaseResponse {
        return if(adminInterfaceImpl.addSong(details)){
            BaseResponse.SuccessResponse("Song ${details.tittle} Has Added", HttpStatusCode.Accepted.toString())
        }
        else{
            throw SongAlreadyExistsException(msg="Song ${details.tittle} Already Exists",HttpStatusCode.BadRequest)
        }
    }
    suspend fun deleteSongService(details: DeleteSong): BaseResponse {
        return if (adminInterfaceImpl.checkSong(details.tittle!!, details.artist!!)
            && adminInterfaceImpl.getSongId(details)!=null ) {
            adminInterfaceImpl.deleteSong(details)
            BaseResponse.SuccessResponse("${details.tittle} Has Deleted ", HttpStatusCode.Accepted.toString())
        }
        else  {
            throw SongNotFoundException("Song ${details.tittle} Does Not Exists", HttpStatusCode.BadRequest)
        }

    }

}