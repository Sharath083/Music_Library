package com.example.utils

import io.ktor.http.*

class SongAlreadyExistsException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()
class SongNotFoundException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()
class ArtistDoesNotExistsException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()
class UserAlreadyExistsException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()
class UserDoesNotExistsException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()
class PlayListNotFoundException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()
class InvalidTokenException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()
class InvalidUserNameOrPasswordException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()
class SessionDataIsNullException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()
class InvalidLoginForAdminException(val msg:String,val statusCode: HttpStatusCode):RuntimeException()



//RequestValidation
class InvalidNameException(val statusCode: HttpStatusCode):RuntimeException()
class InvalidPasswordException(val statusCode: HttpStatusCode):RuntimeException()
class InvalidEmailException(val statusCode: HttpStatusCode):RuntimeException()
class InvalidEmailFormatException(val statusCode: HttpStatusCode):RuntimeException()

class InvalidArtistException(val statusCode: HttpStatusCode):RuntimeException()
class InvalidSongException(val statusCode: HttpStatusCode):RuntimeException()
class InvalidDurationException(val statusCode: HttpStatusCode):RuntimeException()
class InvalidPlayListException(val statusCode: HttpStatusCode):RuntimeException()






