package de.hennihaus.plugins

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCors() {
    val allowedHost = getProperty<String>(key = "ktor.deployment.cors.allowedHost")
    val allowedProtocol = getProperty<String>(key = "ktor.deployment.cors.allowedProtocol")
    install(CORS) {
        allowHost(host = allowedHost, schemes = listOf(allowedProtocol))

        allowHeader(header = HttpHeaders.ContentType)

        allowMethod(method = HttpMethod.Options)
        allowMethod(method = HttpMethod.Get)
    }
}
