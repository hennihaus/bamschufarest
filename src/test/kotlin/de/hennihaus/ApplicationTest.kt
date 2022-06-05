package de.hennihaus

import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test

class ApplicationTest {
    @Test
    fun `should return 200 and example message when root endpoint is available`() = testApplication {
        application {
            routing {
                get(ROOT_ENDPOINT_PATH) { call.respond(message = ROOT_ENDPOINT_MESSAGE) }
            }
        }

        val response = client.get(urlString = ROOT_ENDPOINT_PATH)

        response shouldHaveStatus HttpStatusCode.OK
        response.bodyAsText() shouldBe ROOT_ENDPOINT_MESSAGE
    }

    companion object {
        private const val ROOT_ENDPOINT_PATH = "/"
        private const val ROOT_ENDPOINT_MESSAGE = "Microservice has started successfully"
    }
}
