package de.hennihaus.plugins

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import de.hennihaus.configurations.Configuration.API_VERSION
import de.hennihaus.routes.registerRatingRoutes
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route

fun Application.configureRouting() {
    val apiVersion = environment.config.property(path = API_VERSION).getString()

    install(plugin = ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            registerModule(JavaTimeModule())
        }
    }
    install(plugin = IgnoreTrailingSlash)
    install(plugin = Routing) {
        route(path = "/$apiVersion") {
            registerRatingRoutes()
        }
    }
}
