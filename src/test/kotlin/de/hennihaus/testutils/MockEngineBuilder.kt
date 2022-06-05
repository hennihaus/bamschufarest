package de.hennihaus.testutils

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object MockEngineBuilder {

    fun getMockEngine(
        content: String = "",
        status: HttpStatusCode = HttpStatusCode.OK,
        headers: Headers = headersOf(HttpHeaders.ContentType to listOf("${ContentType.Application.Json}")),
        assertions: (request: HttpRequestData) -> Unit = {},
    ) = MockEngine {
        assertions(it)
        respond(
            content = content,
            status = status,
            headers = headers,
        )
    }
}
