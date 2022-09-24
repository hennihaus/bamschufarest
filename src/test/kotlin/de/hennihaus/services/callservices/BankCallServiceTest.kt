package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Bank
import de.hennihaus.bamdatamodel.objectmothers.BankObjectMother.getSchufaBank
import de.hennihaus.objectmothers.ConfigurationObjectMother.getConfigBackendConfiguration
import de.hennihaus.services.callservices.resources.BankPaths.BANKS_PATH
import de.hennihaus.testutils.MockEngineBuilder.getMockEngine
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BankCallServiceTest {

    private val config = getConfigBackendConfiguration()
    private val defaultBankId = getSchufaBank().uuid

    private lateinit var engine: MockEngine
    private lateinit var classUnderTest: BankCallService

    @Nested
    inner class GetBankById {
        @Test
        fun `should return a bank by id and build call correctly`() = runBlocking {
            engine = getMockEngine(
                content = Json.encodeToString(
                    value = getSchufaBank(),
                ),
                assertions = {
                    it.method shouldBe HttpMethod.Get
                    it.url shouldBe Url(
                        urlString = buildString {
                            append(config.protocol)
                            append("://")
                            append(config.host)
                            append(":")
                            append(config.port)
                            append("/")
                            append(config.apiVersion)
                            append(BANKS_PATH)
                            append("/")
                            append(defaultBankId)
                        },
                    )
                    it.headers[HttpHeaders.Accept] shouldBe "${ContentType.Application.Json}"
                },
            )
            classUnderTest = BankCallService(
                defaultBankId = "$defaultBankId",
                engine = engine,
                config = config,
            )

            val result: Bank = classUnderTest.getBankById()

            result shouldBe getSchufaBank()
        }

        @Test
        fun `should throw an exception and request one time when bad request error occurs`() = runBlocking {
            var counter = 0
            engine = getMockEngine(
                status = HttpStatusCode.BadRequest,
                assertions = { counter++ },
            )
            classUnderTest = BankCallService(
                defaultBankId = "$defaultBankId",
                engine = engine,
                config = config,
            )

            val result = shouldThrowExactly<ClientRequestException> {
                classUnderTest.getBankById()
            }

            result.response shouldHaveStatus HttpStatusCode.BadRequest
            counter shouldBe 1
        }

        @Test
        fun `should throw an exception and request three times when internal server error occurs`() = runBlocking {
            var counter = 0
            engine = getMockEngine(
                status = HttpStatusCode.InternalServerError,
                assertions = { counter++ },
            )
            classUnderTest = BankCallService(
                defaultBankId = "$defaultBankId",
                engine = engine,
                config = config.copy(
                    maxRetries = 2,
                ),
            )

            val result = shouldThrowExactly<ServerResponseException> {
                classUnderTest.getBankById()
            }

            result.response shouldHaveStatus HttpStatusCode.InternalServerError
            counter shouldBe 3
        }
    }
}
