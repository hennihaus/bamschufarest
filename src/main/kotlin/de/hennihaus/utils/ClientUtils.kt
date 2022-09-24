package de.hennihaus.utils

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.configureMonitoring() = install(plugin = Logging) {
    logger = Logger.DEFAULT
    level = LogLevel.INFO
}

fun HttpClientConfig<*>.configureRetryBehavior(maxRetries: Int) = install(plugin = HttpRequestRetry) {
    retryOnServerErrors(maxRetries = maxRetries)
    exponentialDelay()
}

fun HttpClientConfig<*>.configureSerialization() {
    install(plugin = ContentNegotiation) {
        json(
            contentType = ContentType.Application.Json,
            json = Json {
                ignoreUnknownKeys = true
            }
        )
    }
    install(plugin = Resources)
}

fun HttpClientConfig<*>.configureDefaultRequests(
    protocol: String,
    host: String,
    port: Int,
    apiVersion: String? = null,
) {
    install(plugin = DefaultRequest) {
        url {
            this.protocol = URLProtocol.createOrDefault(name = protocol)
            this.host = host
            this.port = port

            apiVersion?.let {
                path("$it/")
            }
        }
        headers {
            appendIfNameAbsent(name = HttpHeaders.ContentType, value = "${ContentType.Application.Json}")
        }
    }
}
