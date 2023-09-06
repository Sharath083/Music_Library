package com.example.plugins

import com.example.exceptions.*
import com.example.model.BaseResponse
import com.example.utils.appconstant.InfoMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.sql.SQLException
import java.util.InputMismatchException

//fun statusHelper(message:String,msgId:Int, status:String):ExceptionResponse{
//    return ExceptionResponse(message,msgId,status)
//}
fun Application.configureStatusPages() {
    install(StatusPages) {
        var statusCode:HttpStatusCode
        var message:String
        var status:String

        //RequestValidation

        exception<Exception>{ call, cause ->
            when(cause){
                is InvalidNameException ->{
                    statusCode=cause.statusCode
                    message= InfoMessage.INVALID_NAME
                    status=cause.statusCode.toString()                }
                is InvalidArtistException ->{
                    statusCode=cause.statusCode
                    message= InfoMessage.INVALID_Artist_Name
                    status=cause.statusCode.toString()
                }
                is InvalidSongException ->{
                    statusCode=cause.statusCode
                    message= InfoMessage.INVALID_SONG_NAME
                    status=cause.statusCode.toString()
                }
                is InvalidDurationException ->{
                    statusCode=cause.statusCode
                    message= InfoMessage.INVALID_DURATION
                    status=cause.statusCode.toString()
                }
                is InvalidPlayListException ->{
                    statusCode=cause.statusCode
                    message= InfoMessage.PLAYLIST_NOT_VALID
                    status=cause.statusCode.toString()
                }
                is InvalidEmailFormatException ->{
                    statusCode=cause.statusCode
                    message= InfoMessage.INVALID_EMAIL_FORMAT
                    status=cause.statusCode.toString()
                }
                is InvalidEmailException ->{
                    statusCode=cause.statusCode
                    message= InfoMessage.INVALID_EMAIL
                    status=cause.statusCode.toString()
                }
                is InvalidPasswordException ->{
                    statusCode=cause.statusCode
                    message= InfoMessage.INVALID_PASSWORD
                    status=cause.statusCode.toString()
                }
                //Routes Exceptions
                is InvalidLoginForAdminException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is SomethingWentWrongException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is SongAlreadyExistsException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is SongNotFoundException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is UserDoesNotExistsException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is InvalidUserNameOrPasswordException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is PlayListNotFoundException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is UserAlreadyExistsException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is ArtistDoesNotExistsException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is SessionDataIsNullException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                is InvalidTokenException ->{
                    statusCode=cause.statusCode
                    message= cause.msg
                    status=cause.statusCode.toString()
                }
                else->{
                    statusCode=HttpStatusCode.InternalServerError
                    message= "${InfoMessage.UNKNOWN_ERROR} $cause"
                    status=statusCode.toString()
                }
            }
            call.respond(statusCode, BaseResponse.ExceptionResponse(
                response = message,
                msgId = statusCode.value,
                status = status
            ))

        }

        //Inbuilt Exceptions
        exception<Throwable> { call, cause ->
            when(call){
                is SQLException->{
                    statusCode=HttpStatusCode.InternalServerError
                    message= cause.message!!
                    status=statusCode.toString()
                }
                is IllegalArgumentException->{
                    statusCode=HttpStatusCode.InternalServerError
                    message= cause.message!!
                    status=statusCode.toString()
                }
                is InputMismatchException->{
                    statusCode=HttpStatusCode.InternalServerError
                    message= cause.message!!
                    status=statusCode.toString()
                }
                is IllegalStateException-> {
                    statusCode = HttpStatusCode.InternalServerError
                    message = cause.message!!
                    status = statusCode.toString()
                }
                else->{
                    statusCode=HttpStatusCode.InternalServerError
                    message= "${InfoMessage.UNKNOWN_ERROR} $cause"
                    status=statusCode.toString()
                }
            }
                call.respond(statusCode, BaseResponse.ExceptionResponse(
                    response = message,
                    msgId = statusCode.value,
                    status = status
                ))
        }
        exception<ExposedSQLException>{ call, cause->
            call.respond(
                HttpStatusCode.InternalServerError, BaseResponse.ExceptionResponse(
                response = "$cause",
                msgId = HttpStatusCode.InternalServerError.value,
                status = HttpStatusCode.InternalServerError.toString()
            ))
        }






//                is SQLException ->
//                    call.respond(Response.Error("$cause " ,HttpStatusCode.InternalServerError.toString()))
//                is IllegalArgumentException ->
//                    call.respond(Response.Error("$cause ",HttpStatusCode.InternalServerError.toString()))
//                is InputMismatchException ->
//                    call.respond(Response.Error("$cause ",HttpStatusCode.InternalServerError.toString()))
//                is IllegalStateException->
//                    call.respond(Response.Error("$cause ",HttpStatusCode.InternalServerError.toString()))
//                else->{
//                    call.respond(Response.Error("$cause ",HttpStatusCode.InternalServerError.toString()))
//                }

//        exception<InvalidUserNameOrPasswordException>{ call, cause->
//            call.respond(SuccessResponse("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
//        }
//        exception<InvalidTokenException> { call, cause->
//        call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
//        }
//        exception<SessionDataIsNullException> { call, cause->
//            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
//        }
//        exception<UserDoesNotExistsException> { call, cause->
//            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
//        }
    }
}
