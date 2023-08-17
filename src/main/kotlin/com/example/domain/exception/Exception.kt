package com.example.domain.exception

class SongAlreadyExistsException(val msg:String):RuntimeException()
class SongNotFoundException(val msg:String):RuntimeException()
class InvalidInputException(val msg:String):RuntimeException()
class ArtistDoesNotExistsException(val msg:String):RuntimeException()
class UserAlreadyExistsException(val msg:String):RuntimeException()
class UserDoesNotExistsException(val msg:String):RuntimeException()
class PlayListNotFoundException(val msg:String):RuntimeException()
class InvalidTokenException(val msg:String):RuntimeException()
class InvalidUserNameOrPasswordException(val msg:String):RuntimeException()
class SessionDataIsNullException(val msg:String):RuntimeException()


