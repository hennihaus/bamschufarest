package de.hennihaus.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.jackson.jackson
import io.ktor.util.appendIfNameAbsent

fun HttpClientConfig<*>.configureMonitoring() = install(plugin = Logging) {
    logger = Logger.DEFAULT
    level = LogLevel.INFO
}

fun HttpClientConfig<*>.configureRetryBehavior(maxRetries: Int) = install(plugin = HttpRequestRetry) {
    retryOnServerErrors(maxRetries = maxRetries)
    exponentialDelay()
}

fun HttpClientConfig<*>.configureSerialization() = install(plugin = ContentNegotiation) {
    jackson {
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }
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
