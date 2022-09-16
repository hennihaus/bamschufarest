package de.hennihaus.testutils

import de.hennihaus.module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
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
            block()
        }
    }
}

val ApplicationTestBuilder.testClient
    get() = createClient {
        install(plugin = ContentNegotiation) {
            json(json = Json)
        }
    }
