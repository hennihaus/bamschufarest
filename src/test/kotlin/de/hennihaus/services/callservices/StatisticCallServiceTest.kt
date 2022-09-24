package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Statistic
import de.hennihaus.bamdatamodel.objectmothers.StatisticObjectMother.getFirstTeamAsyncBankStatistic
import de.hennihaus.objectmothers.ConfigurationObjectMother.getConfigBackendConfiguration
import de.hennihaus.services.callservices.resources.StatisticPaths.INCREMENT_PATH
import de.hennihaus.services.callservices.resources.StatisticPaths.STATISTICS_PATH
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

class StatisticCallServiceTest {

    private lateinit var classUnderTest: StatisticCallService

    @Nested
    inner class IncrementStatistic {
        @Test
        fun `should increment and return a statistic and build call correctly`() = runBlocking {
            val (bankId, teamId) = getFirstTeamAsyncBankStatistic()
            val (protocol, host, port, apiVersion) = getConfigBackendConfiguration()
            val engine = getMockEngine(
                content = Json.encodeToString(
                    value = getFirstTeamAsyncBankStatistic(
                        requestsCount = 1L
                    ),
                ),
                assertions = {
                    it.method shouldBe HttpMethod.Patch
                    it.url shouldBe Url(
                        urlString = "$protocol://$host:$port/$apiVersion$STATISTICS_PATH$INCREMENT_PATH",
                    )
                    it.headers[HttpHeaders.Accept] shouldBe "${ContentType.Application.Json}"
                    // it.body shouldBe getFirstTeamAsyncBankStatistic().toBody()
                },
            )
            classUnderTest = StatisticCallService(
                engine = engine,
                config = getConfigBackendConfiguration(),
            )

            val result: Statistic = classUnderTest.incrementStatistic(
                bankId = bankId,
                teamId = teamId,
            )

            result shouldBe getFirstTeamAsyncBankStatistic(requestsCount = 1L)
        }

        @Test
        fun `should throw an exception and request one time when bad request error occurs`() = runBlocking {
            var counter = 0
            val (bankId, teamId) = getFirstTeamAsyncBankStatistic()
            val engine = getMockEngine(
                status = HttpStatusCode.BadRequest,
                assertions = { counter++ },
            )
            classUnderTest = StatisticCallService(
                engine = engine,
                config = getConfigBackendConfiguration(),
            )

            val result = shouldThrowExactly<ClientRequestException> {
                classUnderTest.incrementStatistic(
                    bankId = bankId,
                    teamId = teamId,
                )
            }

            result.response shouldHaveStatus HttpStatusCode.BadRequest
            counter shouldBe 1
        }

        @Test
        fun `should throw an exception and request three times when internal server error occurs`() = runBlocking {
            var counter = 0
            val (bankId, teamId) = getFirstTeamAsyncBankStatistic()
            val engine = getMockEngine(
                status = HttpStatusCode.InternalServerError,
                assertions = { counter++ },
            )
            classUnderTest = StatisticCallService(
                engine = engine,
                config = getConfigBackendConfiguration(
                    maxRetries = 2,
                ),
            )

            val result = shouldThrowExactly<ServerResponseException> {
                classUnderTest.incrementStatistic(
                    bankId = bankId,
                    teamId = teamId,
                )
            }

            result.response shouldHaveStatus HttpStatusCode.InternalServerError
            counter shouldBe 3
        }
    }
}
