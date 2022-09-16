package de.hennihaus.plugins

import de.hennihaus.configurations.Configuration.API_VERSION
import de.hennihaus.routes.registerRatingRoutes
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import kotlinx.serialization.json.Json

fun Application.configureRouting() {
    val apiVersion = environment.config.property(path = API_VERSION).getString()

    install(Resources)
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
        )
    }
    install(IgnoreTrailingSlash)
    install(Routing) {
        route(path = "/$apiVersion") {
            registerRatingRoutes()
        }
    }
}
