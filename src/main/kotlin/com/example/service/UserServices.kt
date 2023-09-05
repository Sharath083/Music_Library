package com.example.service

import com.example.data.model.*
import com.example.repositories.AdminInterfaceImpl

import com.example.repositories.UserInterfaceImpl
import com.example.utils.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class UserServices : KoinComponent {
    private val userInterfaceImpl by inject<UserInterfaceImpl>()

    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"))
    }
    suspend fun userRegistrationService(details: UserRegistration): SuccessResponse {
        return if(userInterfaceImpl.checkUser(details.name!!,details.email!!)) {
            userInterfaceImpl.userRegistration(details)
            SuccessResponse("Registered Successfully",HttpStatusCode.Created.toString())
        }
        else{
            throw UserAlreadyExistsException("${details.name} or ${details.email} Already Exists ",HttpStatusCode.BadRequest)
        }
    }
    suspend fun userLoginCheckService(name: String, password: String):SuccessResponse{
        return if(userInterfaceImpl.userLoginCheck(name,password)!=null){
            SuccessResponse("Login Success",HttpStatusCode.Accepted.toString())
        }
        else{
            throw InvalidUserNameOrPasswordException("Invalid Login details",HttpStatusCode.BadRequest)
        }
    }
    suspend fun getUserIdService(name: String): UUID {
        return userInterfaceImpl.getUserId(name)
            ?:throw UserDoesNotExistsException("USER DOES NOT EXISTS WITH NAME $name",HttpStatusCode.BadRequest)
    }
    suspend fun filterByArtistService(artist: String):OutputList<List<InputSong>> {
        userInterfaceImpl.filterByArtist(artist).apply {
            return if (this.isNotEmpty()) {
                OutputList(this, HttpStatusCode.Accepted.toString())
            } else {
                throw ArtistDoesNotExistsException("$artist Does Not Exists", HttpStatusCode.BadRequest)
            }
        }
    }

    suspend fun addToPlayList(details: AddToPlayList, usersId:UUID): SuccessResponse {

        return if(userInterfaceImpl.checkSongInDb(details.song!!)
            && userInterfaceImpl.checkSongInPlayList(details.song!!,details.playList!!,usersId)){
            userInterfaceImpl.addToPlayList(details,usersId)
            SuccessResponse("Song ${details.song} Added To PlayList ${details.playList}",HttpStatusCode.Accepted.toString())
        }
        else{
            throw SomethingWentWrongException("Error While Adding To PlayList",HttpStatusCode.InternalServerError)
        }
//        else if( userInterfaceImpl.checkSongInPlayList(details.song!!,details.playList!!,usersId)){
//            userInterfaceImpl.addToPlayList(details,usersId)
//            SuccessResponse("Song ${details.song} Added To PlayList ${details.playList}",HttpStatusCode.Accepted.toString())
//        }

    }
    suspend fun removeFromPlayListService(details: RemoveFromPlayList,usersId:UUID): SuccessResponse {
        return when {
            userInterfaceImpl.checkSongInPlayList(details.song!!, details.playList!!, usersId) ->
                throw SongNotFoundException("${details.song} Does Not Exists in PlayList", HttpStatusCode.BadRequest)

            userInterfaceImpl.removeFromPlayList(details, usersId) ->
                SuccessResponse(
                    "Song ${details.song} Has Removed From PlayList ${details.playList}",
                    HttpStatusCode.Accepted.toString()
                )
            else -> {
                throw SomethingWentWrongException("Something Went Wrong While Removing Song",HttpStatusCode.InternalServerError)
            }
        }
    }
    suspend fun viewPlayListService(playList: String, userId: UUID): OutputList<List<InputSong>>{
        userInterfaceImpl.viewPlayList(playList,userId).apply {
            return when {
                this.isNotEmpty()->
                    OutputList(this, HttpStatusCode.Accepted.toString())
                else->
                    throw PlayListNotFoundException("$playList Does Not Exists", HttpStatusCode.BadRequest)
            }
        }
    }
    suspend fun deleteAccountService(userId: UUID):SuccessResponse{
        userInterfaceImpl.deleteAccount(userId).apply {
            return SuccessResponse("Account Has Deleted",HttpStatusCode.Accepted.toString())
        }

    }
}
