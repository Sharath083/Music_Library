package com.example.plugins

import com.example.data.response.Response
import com.example.domain.exception.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
        exception<InvalidUserNameOrPasswordException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
        }
        exception<InvalidInputException>{ call, cause->
            call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.BadRequest.toString()))
        }
        exception<InvalidTokenException> {call, cause->
        call.respond(Response.Error("$cause ${cause.msg}",HttpStatusCode.Unauthorized.toString()))
        }
        exception<SessionDataIsNullException> {call, cause->
            call.respond(Response.Error("$cause ",HttpStatusCode.Unauthorized.toString()))
        }
        exception<UserDoesNotExistsException> {call, cause->
            call.respond(Response.Error("$cause ",HttpStatusCode.Unauthorized.toString()))
        }
    }
}
