package com.example.plugins

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
                throw InvalidInputException("Give Proper Input To Delete")
            }
            else{
                ValidationResult.Valid
            }
        }
        validate<InputSong> {
            if(it.artist=="" || it.artist==null || it.tittle=="" || it.tittle==null||it.duration==""||it.duration==null){
                throw InvalidInputException("Give Proper Input Add")
            }
            else{
                ValidationResult.Valid
            }
        }
        validate<ArtistData> {
            if(it.artist ==""||it.artist==null){
                throw InvalidInputException("Give Proper Input To Filter")
            }
            else{
                ValidationResult.Valid
            }
        }
        validate<AddToPlayList> {
            if(it.playList=="" || it.playList==null || it.song=="" || it.song==null){
                throw InvalidInputException("Give Proper Input Add To PlayList")
            }
            else{
                ValidationResult.Valid
            }
        }
        validate<RemoveFromPlayList> {
            if(it.playList=="" || it.playList==null || it.song=="" || it.song==null){
                throw InvalidInputException("Give Proper Input Add To PlayList")
            }
            else{
                ValidationResult.Valid
            }
        }
        validate<AdminLogin> {
            if (it.name == "" || it.name == null || it.password == "" || it.password == null) {
                throw InvalidInputException("Give Proper Input To Login")
            } else {
                ValidationResult.Valid
            }
        }
        validate<UserRegistration> {
            if (it.name == "" || it.name == null||it.email == "" || it.email == null|| !Methods().isValidEmail(it.email) || it.password == "" || it.password == null) {
                throw InvalidInputException("Give Proper Input To Register")
            } else {
                ValidationResult.Valid
            }
        }
        validate<UserLogin> {
            if (it.name == "" || it.name == null || it.password == "" || it.password == null) {
                throw InvalidInputException("Give Proper UserName and Password")
            } else {
                ValidationResult.Valid
            }
        }
    }

}
