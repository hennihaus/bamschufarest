package de.hennihaus.testutils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import de.hennihaus.module
import io.kotest.extensions.time.withConstantNow
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.jackson.jackson
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import org.koin.core.module.Module
import java.time.LocalDateTime
import io.ktor.server.testing.testApplication as ktorTestApplication

object KtorTestBuilder {

    private const val TEST_CONFIG_FILE = "application-test.conf"

    fun testApplicationWith(vararg mockModules: Module, block: suspend ApplicationTestBuilder.() -> Unit) {
        ktorTestApplication {
            environment {
                config = ApplicationConfig(configPath = TEST_CONFIG_FILE)
            }
            application {
                module(
                    configFilePath = TEST_CONFIG_FILE,
                    koinModules = mockModules,
                )
            }
            withConstantNow(now = LocalDateTime.now()) {
                block()
            }
        }
    }
}

val ApplicationTestBuilder.testClient
    get() = createClient {
        install(plugin = ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                registerModules(JavaTimeModule())
            }
        }
    }
