package com.example.plugins

import com.example.data.model.Response
import com.example.domain.exception.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.sql.SQLException
import java.util.InputMismatchException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when(call){
                is SQLException ->
                    call.respond(Response.Error("$cause " ,HttpStatusCode.InternalServerError.toString()))
                is IllegalArgumentException ->
                    call.respond(Response.Error("$cause ",HttpStatusCode.InternalServerError.toString()))
                is InputMismatchException ->
                    call.respond(Response.Error("$cause ",HttpStatusCode.InternalServerError.toString()))
                is IllegalStateException->
                    call.respond(Response.Error("$cause ",HttpStatusCode.InternalServerError.toString()))
                else->{
                    call.respond(Response.Error("$cause ",HttpStatusCode.InternalServerError.toString()))
                }
            }
        }
        exception<ExposedSQLException>{ call, cause->
            call.respond(Response.Error("$cause",HttpStatusCode.InternalServerError.toString()))
        }

        //RequestValidation
        exception<InvalidArtistException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.BadRequest.toString()))
        }
        exception<InvalidSongException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.BadRequest.toString()))
        }
        exception<InvalidDurationException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.BadRequest.toString()))
        }
        exception<InvalidPlayListException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.BadRequest.toString()))
        }
        exception<InvalidNameException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.BadRequest.toString()))
        }
        exception<InvalidEmailException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.BadRequest.toString()))
        }
        exception<InvalidPasswordException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.BadRequest.toString()))
        }



        exception<InvalidUserNameOrPasswordException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
        }
        exception<InvalidTokenException> {call, cause->
        call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
        }
        exception<SessionDataIsNullException> {call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
        }
        exception<UserDoesNotExistsException> {call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
        }
    }
}
