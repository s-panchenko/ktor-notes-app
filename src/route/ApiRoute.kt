package me.spanchenko.route

import io.ktor.routing.*

fun Route.apiRoutes() {
    route("/api/v1") {
        noteRoutes()
    }
}
