package me.spanchenko.route

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.spanchenko.dto.NoteDto
import me.spanchenko.dto.ResponseData
import me.spanchenko.dto.toResponseData
import me.spanchenko.service.NoteService
import org.koin.ktor.ext.inject

fun Route.noteRoutes() {
    val service by inject<NoteService>()

    get("/notes/{id}") {
        call.apply {
            val noteId = idParamOrThrow()
            respond(HttpStatusCode.OK, service.findById(noteId).toResponseData())
        }
    }

    get("/notes") {
        call.respond(HttpStatusCode.OK, service.findAll().toResponseData())
    }

    post("/notes") {
        call.apply {
            val receivedNote = receive<NoteDto>()
            val savedNote = service.save(receivedNote)
            respond(HttpStatusCode.Created, savedNote.toResponseData())
        }
    }

    patch("/notes/{id}") {
        call.apply {
            val noteId = idParamOrThrow()
            val receivedNote = receive<NoteDto>()
            val updatedNote = service.update(noteId, receivedNote)
            respond(HttpStatusCode.OK, updatedNote.toResponseData())
        }
    }

    delete("/notes/{id}") {
        call.apply {
            val noteId = idParamOrThrow()
            service.delete(noteId)
            respond(HttpStatusCode.NoContent)
        }
    }
}

fun ApplicationCall.idParamOrThrow() = parameters["id"]?.toLongOrNull() ?: throw NotFoundException()
