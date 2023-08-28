package com.example.plugins

import com.example.utils.appconstant.InfoMessage.PLAYLIST_NOT_VALID
import com.example.data.model.*
import com.example.domain.exception.*
import com.example.utils.appconstant.InfoMessage.INVALID_Artist_Name
import com.example.utils.appconstant.InfoMessage.INVALID_DURATION
import com.example.utils.appconstant.InfoMessage.INVALID_EMAIL
import com.example.utils.appconstant.InfoMessage.INVALID_EMAIL_FORMAT
import com.example.utils.appconstant.InfoMessage.INVALID_NAME
import com.example.utils.appconstant.InfoMessage.INVALID_PASSWORD
import com.example.utils.appconstant.InfoMessage.INVALID_SONG_Name
import com.example.utils.helperfunctions.HelperMethods
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation(){


    install(RequestValidation){
        validate<DeleteSong> {
            when{
                it.artist.isNullOrBlank()->throw InvalidArtistException(INVALID_Artist_Name)
                it.tittle.isNullOrBlank()->throw InvalidSongException(INVALID_SONG_Name)
                else->ValidationResult.Valid
            }
        }
        validate<InputSong> {
            when{
                it.artist.isNullOrBlank()->throw InvalidArtistException(INVALID_Artist_Name)
                it.tittle.isNullOrBlank()->throw InvalidSongException(INVALID_SONG_Name)
                it.duration.isNullOrBlank()->throw InvalidDurationException(INVALID_DURATION)
                else->ValidationResult.Valid
            }
        }
        validate<ArtistData> {
            when {
                it.artist.isNullOrBlank()->throw InvalidArtistException(INVALID_Artist_Name)
                else -> ValidationResult.Valid
            }
        }
        validate<AddToPlayList> {
            when{
                it.playList.isNullOrBlank()->throw InvalidPlayListException(PLAYLIST_NOT_VALID)
                it.song.isNullOrBlank()->throw InvalidSongException(INVALID_SONG_Name)
                else->ValidationResult.Valid
            }
        }
        validate<RemoveFromPlayList> {
            when{
                it.playList.isNullOrBlank()->throw InvalidPlayListException(PLAYLIST_NOT_VALID)
                it.song.isNullOrBlank()->throw InvalidSongException(INVALID_SONG_Name)
                else->ValidationResult.Valid
            }
        }

        validate<AdminLogin> {
            when{
                it.name.isNullOrBlank()->throw InvalidNameException(INVALID_NAME)
                it.password.isNullOrBlank()->throw InvalidPasswordException(INVALID_PASSWORD)
                else->ValidationResult.Valid
            }
        }
        validate<UserRegistration> {
            when{
                it.name.isNullOrBlank()->throw InvalidNameException(INVALID_NAME)
                it.password.isNullOrBlank()->throw InvalidPasswordException(INVALID_PASSWORD)
                it.email.isNullOrBlank()->throw InvalidEmailException(INVALID_EMAIL)
                !HelperMethods().isValidEmail(it.email)->throw InvalidEmailException(INVALID_EMAIL_FORMAT)
                else->ValidationResult.Valid
            }

        }
        validate<UserLogin> {
            when{
                it.name.isNullOrBlank()->throw InvalidNameException(INVALID_NAME)
                it.password.isNullOrBlank()->throw InvalidPasswordException(INVALID_PASSWORD)
                else->ValidationResult.Valid
            }
        }
        validate<ViewPlayList> {
            when{
                it.playlistName.isNullOrBlank()->throw InvalidPlayListException(PLAYLIST_NOT_VALID)
                else->ValidationResult.Valid
            }
        }
    }

}
