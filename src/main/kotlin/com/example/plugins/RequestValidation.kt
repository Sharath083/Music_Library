package com.example.plugins

import com.example.data.constants.APIConstantsExceptions.INVALID_ADMIN_LOGIN_DETAILS
import com.example.data.constants.APIConstantsExceptions.INVALID_ARTIST
import com.example.data.constants.APIConstantsExceptions.INVALID_DELETE_DATA
import com.example.data.constants.APIConstantsExceptions.INVALID_DETAILS_OF_SONG
import com.example.data.constants.APIConstantsExceptions.INVALID_LOGIN_DETAILS
import com.example.data.constants.APIConstantsExceptions.INVALID_REGISTRATION
import com.example.data.constants.APIConstantsExceptions.INVALID_SONG_DATA
import com.example.data.constants.APIConstantsExceptions.INVALID_SONG_TO_REMOVE
import com.example.data.constants.APIConstantsExceptions.PLAYLIST_NOT_VALID
import com.example.data.request.*
import com.example.domain.exception.InvalidInputException
import com.example.utils.Methods
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import org.h2.engine.User

fun Application.configureRequestValidation(){

    install(RequestValidation){
        validate<DeleteSong> {
            if(it.artist=="" || it.artist==null || it.tittle=="" || it.tittle==null ){
                throw InvalidInputException(INVALID_DELETE_DATA)
            }
            else{
                ValidationResult.Valid
            }
        }
        validate<InputSong> {
            if(it.artist=="" || it.artist==null || it.tittle=="" || it.tittle==null||it.duration==""||it.duration==null){
                throw InvalidInputException(INVALID_DETAILS_OF_SONG)
            }
            else{
                ValidationResult.Valid
            }
        }
        validate<ArtistData> {
            if(it.artist ==""||it.artist==null){
                throw InvalidInputException(INVALID_ARTIST)
            }
            else{
                ValidationResult.Valid
            }
        }
        validate<AddToPlayList> {
            if(it.playList=="" || it.playList==null || it.song=="" || it.song==null){
                throw InvalidInputException(INVALID_SONG_DATA)
            }
            else{
                ValidationResult.Valid
            }
        }
        validate<RemoveFromPlayList> {
            if(it.playList=="" || it.playList==null || it.song=="" || it.song==null){
                throw InvalidInputException(INVALID_SONG_TO_REMOVE)
            }
            else{
                ValidationResult.Valid
            }
        }

        validate<AdminLogin> {
            if (it.name == "" || it.name == null || it.password == "" || it.password == null) {
                throw InvalidInputException(INVALID_ADMIN_LOGIN_DETAILS)
            } else {
                ValidationResult.Valid
            }
        }
        validate<UserRegistration> {
            if (it.name == "" || it.name == null||it.email == "" || it.email == null|| !Methods().isValidEmail(it.email) || it.password == "" || it.password == null) {
                throw InvalidInputException(INVALID_REGISTRATION)
            } else {
                ValidationResult.Valid
            }
        }
        validate<UserLogin> {
            if (it.name == "" || it.name == null || it.password == "" || it.password == null) {
                throw InvalidInputException(INVALID_LOGIN_DETAILS)
            } else {
                ValidationResult.Valid
            }
        }
        validate<ViewPlayList> {
            if (it.playlistName == "" || it.playlistName == null) {
                throw InvalidInputException(PLAYLIST_NOT_VALID)
            } else {
                ValidationResult.Valid
            }
        }
    }

}
