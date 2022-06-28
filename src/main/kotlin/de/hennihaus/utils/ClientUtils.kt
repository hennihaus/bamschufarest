package de.hennihaus.utils

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
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