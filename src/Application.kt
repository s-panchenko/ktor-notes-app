package me.spanchenko

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import me.spanchenko.config.DatabaseInitializer
import me.spanchenko.module.appModule
import me.spanchenko.route.apiRoutes
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(Koin) {
        modules(appModule)
    }

    val databaseInitializer by inject<DatabaseInitializer>()
    databaseInitializer.initDatabase()

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(DefaultHeaders)

    install(ContentNegotiation) {
        json()
    }

    routing {
        apiRoutes()

        install(StatusPages) {
            exception<NotFoundException> {
                call.respond(HttpStatusCode.NotFound)
            }
            exception<EntityNotFoundException> {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

