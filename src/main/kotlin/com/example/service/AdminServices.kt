package com.example.service


import com.example.model.DeleteSong
import com.example.model.InputSong
import com.example.repositories.AdminInterfaceImpl
import com.example.exceptions.InvalidLoginForAdminException
import com.example.exceptions.SongAlreadyExistsException
import com.example.exceptions.SongNotFoundException
import com.example.model.BaseResponse
import com.example.model.SuccessResponse
import com.example.utils.helperfunctions.HelperMethods
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class AdminServices: KoinComponent {
    private val adminInterfaceImpl by inject<AdminInterfaceImpl>()
    private val helperFunction by inject<HelperMethods>()




    fun adminLoginService(name: String, password: String): SuccessResponse {
        return if(adminInterfaceImpl.adminLoginCheck(name,password)){
            SuccessResponse(helperFunction.tokenGeneratorAdmin(), HttpStatusCode.Created.toString())
        }
        else{
            throw InvalidLoginForAdminException("Imposter", HttpStatusCode.Unauthorized)
        }
    }
    suspend fun songAddService(details: InputSong): SuccessResponse {
        return if(!adminInterfaceImpl.checkSong(details.song!!,details.artist!!)
            && adminInterfaceImpl.addSong(details)){
            SuccessResponse("Song ${details.song} Has Added", HttpStatusCode.Accepted.toString())
        }
        else{
            throw SongAlreadyExistsException(msg="Song ${details.song} Already Exists",HttpStatusCode.BadRequest)
        }
    }
    suspend fun deleteSongService(details: DeleteSong): SuccessResponse {
        return if (adminInterfaceImpl.checkSong(details.tittle!!, details.artist!!)
            && adminInterfaceImpl.deleteSong(details) ) {
            SuccessResponse("${details.tittle} Has Deleted", HttpStatusCode.Accepted.toString())
        }
        else  {
            throw SongNotFoundException("Song ${details.tittle} Does Not Exists", HttpStatusCode.BadRequest)
        }
    }

}