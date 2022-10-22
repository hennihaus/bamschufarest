package de.hennihaus.plugins

import de.hennihaus.configurations.Configuration.ALLOWED_HOST
import de.hennihaus.configurations.Configuration.ALLOWED_PROTOCOL
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCors() {
    val allowedProtocol = environment.config.property(path = ALLOWED_PROTOCOL).getString()
    val allowedHost = environment.config.property(path = ALLOWED_HOST).getString()

    install(plugin = CORS) {
        allowHost(host = allowedHost, schemes = listOf(allowedProtocol))

        allowHeader(header = HttpHeaders.ContentType)

        allowMethod(method = HttpMethod.Options)
        allowMethod(method = HttpMethod.Get)
    }
}
