package com.example.plugins

import com.example.data.model.*
import com.example.di.service.UserServices
import com.example.utils.*
import com.example.utils.helperfunctions.HelperMethods
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation(){


    install(RequestValidation){
        validate<DeleteSong> {
            when{
                it.artist.isNullOrBlank()->throw InvalidArtistException(HttpStatusCode.BadRequest)
                it.tittle.isNullOrBlank()->throw InvalidSongException(HttpStatusCode.BadRequest)
                else->ValidationResult.Valid
            }
        }
        validate<InputSong> {
            when{
                it.artist.isNullOrBlank()->throw InvalidArtistException(HttpStatusCode.BadRequest)
                it.tittle.isNullOrBlank()->throw InvalidSongException(HttpStatusCode.BadRequest)
                it.duration.isNullOrBlank()->throw InvalidDurationException(HttpStatusCode.BadRequest)
                else->ValidationResult.Valid
            }
        }
        validate<ArtistData> {
            when {
                it.artist.isNullOrBlank()->throw InvalidArtistException(HttpStatusCode.BadRequest)
                else -> ValidationResult.Valid
            }
        }
        validate<AddToPlayList> {
            when{
                it.playList.isNullOrBlank()->throw InvalidPlayListException(HttpStatusCode.BadRequest)
                it.song.isNullOrBlank()->throw InvalidSongException(HttpStatusCode.BadRequest)
                else->ValidationResult.Valid
            }
        }
        validate<RemoveFromPlayList> {
            when{
                it.playList.isNullOrBlank()->throw InvalidPlayListException(HttpStatusCode.BadRequest)
                it.song.isNullOrBlank()->throw InvalidSongException(HttpStatusCode.BadRequest)
                else->ValidationResult.Valid
            }
        }

        validate<AdminLogin> {
            when{
                it.name!!.isBlank() ->throw InvalidNameException(HttpStatusCode.BadRequest)
                it.password.isNullOrBlank()->throw InvalidPasswordException(HttpStatusCode.BadRequest)
                else->ValidationResult.Valid
            }
        }
        validate<UserRegistration> {
            when{
                it.name.isNullOrBlank()->throw InvalidNameException(HttpStatusCode.BadRequest)
                it.password.isNullOrBlank()->throw InvalidPasswordException(HttpStatusCode.BadRequest)
                it.email.isNullOrBlank()->throw InvalidEmailException(HttpStatusCode.BadRequest)
                !UserServices().isValidEmail(it.email)->throw InvalidEmailFormatException(HttpStatusCode.BadRequest)
                else->ValidationResult.Valid
            }

        }
        validate<UserLogin> {
            when{
                it.name.isNullOrBlank()->throw InvalidNameException(HttpStatusCode.BadRequest)
                it.password.isNullOrBlank()->throw InvalidPasswordException(HttpStatusCode.BadRequest)
                else->ValidationResult.Valid
            }
        }
        validate<ViewPlayList> {
            when{
                it.playlistName.isNullOrBlank()->throw InvalidPlayListException(HttpStatusCode.BadRequest)
                else->ValidationResult.Valid
            }
        }
    }

}
