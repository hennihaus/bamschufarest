package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Team
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.getFirstTeam
import de.hennihaus.objectmothers.ConfigurationObjectMother.getConfigBackendConfiguration
import de.hennihaus.objectmothers.TeamPaginationObjectMother.getDefaultTeamPagination
import de.hennihaus.services.callservices.paths.ConfigBackendPaths.PASSWORD_QUERY_PARAMETER
import de.hennihaus.services.callservices.paths.ConfigBackendPaths.TEAMS_PATH
import de.hennihaus.services.callservices.paths.ConfigBackendPaths.USERNAME_QUERY_PARAMETER
import de.hennihaus.testutils.MockEngineBuilder.getMockEngine
import de.hennihaus.testutils.bamObjectMapper
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactly
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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TeamCallServiceTest {

    private val config = getConfigBackendConfiguration()

    private lateinit var engine: MockEngine
    private lateinit var classUnderTest: TeamCallService

    @Nested
    inner class GetTeams {
        @Test
        fun `should return teams and build call correctly`() = runBlocking {
            val (_, _, username, password) = getFirstTeam()
            engine = getMockEngine(
                content = bamObjectMapper().writeValueAsString(getDefaultTeamPagination()),
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
                            append("/")
                            append(TEAMS_PATH)
                            append("?")
                            append(USERNAME_QUERY_PARAMETER)
                            append("=")
                            append(username)
                            append("&")
                            append(PASSWORD_QUERY_PARAMETER)
                            append("=")
                            append(password)
                        },
                    )
                    it.headers[HttpHeaders.Accept] shouldBe "${ContentType.Application.Json}"
                },
            )
            classUnderTest = TeamCallService(
                engine = engine,
                config = config,
            )

            val result: List<Team> = classUnderTest.getTeams(
                username = username,
                password = password,
            )

            result shouldContainExactly getDefaultTeamPagination().items
        }

        @Test
        fun `should throw an exception and request one time when bad request error occurs`() = runBlocking {
            val (_, _, username, password) = getFirstTeam()
            var counter = 0
            engine = getMockEngine(
                status = HttpStatusCode.BadRequest,
                assertions = { counter++ },
            )
            classUnderTest = TeamCallService(
                engine = engine,
                config = config,
            )

            val result = shouldThrowExactly<ClientRequestException> {
                classUnderTest.getTeams(
                    username = username,
                    password = password,
                )
            }

            result.response shouldHaveStatus HttpStatusCode.BadRequest
            counter shouldBe 1
        }

        @Test
        fun `should throw an exception and request three times when internal server error occurs`() = runBlocking {
            val (_, _, username, password) = getFirstTeam()
            var counter = 0
            engine = getMockEngine(
                status = HttpStatusCode.InternalServerError,
                assertions = { counter++ },
            )
            classUnderTest = TeamCallService(
                engine = engine,
                config = config.copy(
                    maxRetries = 2,
                ),
            )

            val result = shouldThrowExactly<ServerResponseException> {
                classUnderTest.getTeams(
                    username = username,
                    password = password,
                )
            }

            result.response shouldHaveStatus HttpStatusCode.InternalServerError
            counter shouldBe 3
        }
    }
}
