package me.spanchenko

import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.ExperimentalSerializationApi
import me.spanchenko.dto.NoteDto
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@ExperimentalSerializationApi
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RoutesTest {
    private val note = NoteDto(id = 1, title = "First Note Title", content = "First Note Content")

    @Test
    @Order(1)
    fun `post route should return create note`() = testApp {
        handleRequest(HttpMethod.Post, "/api/v1/notes") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(note.toRequestString())
        }.apply {
            response.status() shouldBe HttpStatusCode.Created
            response.content shouldBe note.toResponseString()
        }
    }

    @Test
    @Order(2)
    fun `get route should return list of notes`() = testApp {
        val expectedResponse = listOf(note).toResponseListString()

        handleRequest(HttpMethod.Get, "/api/v1/notes").apply {
            response.status() shouldBe HttpStatusCode.OK
            response.content shouldBe expectedResponse
        }
    }

    @Test
    @Order(3)
    fun `patch route should return updated note`() = testApp {
        val updatedNote = note.copy(
            title = "First Note Title Updated",
            content = "First Note Content Updated"
        )

        handleRequest(HttpMethod.Patch, "/api/v1/notes/${note.id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(updatedNote.toRequestString())
        }.apply {
            response.status() shouldBe HttpStatusCode.OK
            response.content shouldBe updatedNote.toResponseString()
        }
    }

    @Test
    @Order(4)
    fun `delete route should delete note`() = testApp {
        handleRequest(HttpMethod.Delete, "/api/v1/notes/${note.id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }.apply {
            response.status() shouldBe HttpStatusCode.NoContent
        }

        handleRequest(HttpMethod.Get, "/api/v1/notes/${note.id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }.apply {
            response.status() shouldBe HttpStatusCode.NotFound
        }
    }

    private fun testApp(testBody: TestApplicationEngine.() -> Unit) {
        withTestApplication({ module(testing = true) }) {
            testBody()
        }
    }
}

