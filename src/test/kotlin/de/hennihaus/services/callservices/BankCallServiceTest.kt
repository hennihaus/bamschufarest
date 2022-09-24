package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Bank
import de.hennihaus.bamdatamodel.objectmothers.BankObjectMother.getSchufaBank
import de.hennihaus.objectmothers.ConfigurationObjectMother.getConfigBackendConfiguration
import de.hennihaus.services.callservices.resources.BankPaths.BANKS_PATH
import de.hennihaus.testutils.MockEngineBuilder.getMockEngine
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
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

    private lateinit var classUnderTest: BankCallService

    @Nested
    inner class GetBankById {
        @Test
        fun `should return a bank by id and build call correctly`() = runBlocking {
            val id = getSchufaBank().uuid
            val (protocol, host, port, apiVersion) = getConfigBackendConfiguration()
            val engine = getMockEngine(
                content = Json.encodeToString(
                    value = getSchufaBank(),
                ),
                assertions = {
                    it.method shouldBe HttpMethod.Get
                    it.url shouldBe Url(
                        urlString = "$protocol://$host:$port/$apiVersion$BANKS_PATH/$id",
                    )
                    it.headers[HttpHeaders.Accept] shouldBe "${ContentType.Application.Json}"
                    // it.body shouldBe getSchufaBank().toBody()
                },
            )
            classUnderTest = BankCallService(
                engine = engine,
                config = getConfigBackendConfiguration(),
            )

            val result: Bank = classUnderTest.getBankById(id = id)

            result shouldBe getSchufaBank()
        }

        @Test
        fun `should throw an exception and request one time when bad request error occurs`() = runBlocking {
            var counter = 0
            val id = getSchufaBank().uuid
            val engine = getMockEngine(
                status = HttpStatusCode.BadRequest,
                assertions = { counter++ },
            )
            classUnderTest = BankCallService(
                engine = engine,
                config = getConfigBackendConfiguration(),
            )

            val result = shouldThrowExactly<ClientRequestException> {
                classUnderTest.getBankById(id = id)
            }

            result.response shouldHaveStatus HttpStatusCode.BadRequest
            counter shouldBe 1
        }

        @Test
        fun `should throw an exception and request three times when internal server error occurs`() = runBlocking {
            var counter = 0
            val id = getSchufaBank().uuid
            val engine = getMockEngine(
                status = HttpStatusCode.InternalServerError,
                assertions = { counter++ },
            )
            classUnderTest = BankCallService(
                engine = engine,
                config = getConfigBackendConfiguration(
                    maxRetries = 2,
                ),
            )

            val result = shouldThrowExactly<ServerResponseException> {
                classUnderTest.getBankById(id = id)
            }

            result.response shouldHaveStatus HttpStatusCode.InternalServerError
            counter shouldBe 3
        }
    }
}
