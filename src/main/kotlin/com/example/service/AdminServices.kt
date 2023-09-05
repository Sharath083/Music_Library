package com.example.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.model.DeleteSong
import com.example.data.model.InputSong
import com.example.data.model.SuccessResponse
import com.example.repositories.AdminInterfaceImpl
import com.example.utils.InvalidLoginForAdminException
import com.example.utils.SongAlreadyExistsException
import com.example.utils.SongNotFoundException
import com.example.config.JWTData
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

    fun adminLoginService(name: String, password: String):SuccessResponse{
        return if(adminInterfaceImpl.adminLoginCheck(name,password)){
            SuccessResponse(tokenGeneratorAdmin(), HttpStatusCode.Created.toString())
        }
        else{
            throw InvalidLoginForAdminException("Imposter", HttpStatusCode.Unauthorized)
        }
    }
    suspend fun songAddService(details: InputSong):SuccessResponse{
        return if(adminInterfaceImpl.addSong(details)){
            SuccessResponse("Song ${details.tittle} Has Added", HttpStatusCode.Accepted.toString())
        }
        else{
            throw SongAlreadyExistsException(msg="Song ${details.tittle} Already Exists",HttpStatusCode.BadRequest)
        }
    }
    suspend fun deleteSongService(details: DeleteSong):SuccessResponse {
        return if (adminInterfaceImpl.checkSong(details.tittle!!, details.artist!!) && adminInterfaceImpl.deleteSong(details)) {
            SuccessResponse("${details.tittle} Has Deleted ", HttpStatusCode.Accepted.toString())
        }
        else  {
            throw SongNotFoundException("Song ${details.tittle} Does Not Exists", HttpStatusCode.BadRequest)
        }

    }

}