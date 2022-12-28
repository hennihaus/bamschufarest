package de.hennihaus.services

import de.hennihaus.bamdatamodel.objectmothers.BankObjectMother.getSchufaBank
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.getFirstTeam
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.getSecondTeam
import de.hennihaus.objectmothers.ConfigurationObjectMother.getConfigBackendConfiguration
import de.hennihaus.services.TrackingService.Companion.TEAM_NOT_FOUND_MESSAGE
import de.hennihaus.services.callservices.StatisticCallService
import de.hennihaus.services.callservices.TeamCallService
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.client.engine.cio.CIO
import io.ktor.server.plugins.NotFoundException
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TrackingServiceTest {

    private val config = getConfigBackendConfiguration()

    private val teamCall = mockk<TeamCallService>()
    private val statisticCall = spyk(
        objToCopy = StatisticCallService(
            defaultBankId = "${getSchufaBank().uuid}",
            engine = CIO.create(),
            config = config,
        )
    )

    private val classUnderTest = TrackingService(
        teamCall = teamCall,
        statisticCall = statisticCall,
    )

    @BeforeEach
    fun init() = clearAllMocks()

    @Nested
    inner class TrackRequest {
        @Test
        fun `should call increment statistic with teamId when username and password in teams response`() = runBlocking {
            val (teamId, _, username, password) = getFirstTeam()
            coEvery { teamCall.getTeams(username = any(), password = any()) } returns listOf(
                getFirstTeam(),
                getSecondTeam(),
            )
            coEvery { statisticCall.incrementStatistic(teamId = any(), bankId = any()) } returns mockk()

            classUnderTest.trackRequest(
                username = username,
                password = password,
            )

            coVerifySequence {
                teamCall.getTeams(username = username, password = password)
                statisticCall.incrementStatistic(teamId = teamId)
            }
        }

        @Test
        fun `should throw an exception and not increment statistic when teams response is empty`() = runBlocking {
            val (_, _, username, password) = getFirstTeam()
            coEvery { teamCall.getTeams(username = any(), password = any()) } returns emptyList()

            val result = shouldThrowExactly<NotFoundException> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            result shouldHaveMessage TEAM_NOT_FOUND_MESSAGE
            coVerify(exactly = 1) { teamCall.getTeams(username = username, password = password) }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception and not increment statistic when username is unknown`() = runBlocking {
            val username = "unknown"
            val password = getFirstTeam().password
            coEvery { teamCall.getTeams(username = any(), password = any()) } returns listOf(getFirstTeam())

            val result = shouldThrowExactly<NotFoundException> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            result shouldHaveMessage TEAM_NOT_FOUND_MESSAGE
            coVerify(exactly = 1) { teamCall.getTeams(username = username, password = password) }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception and not increment statistic when password is unknown`() = runBlocking {
            val username = getFirstTeam().username
            val password = "unknown"
            coEvery { teamCall.getTeams(username = any(), password = any()) } returns listOf(getFirstTeam())

            val result = shouldThrowExactly<NotFoundException> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            result shouldHaveMessage TEAM_NOT_FOUND_MESSAGE
            coVerify(exactly = 1) { teamCall.getTeams(username = username, password = password) }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception when error occurs while teams call`() = runBlocking {
            val (_, _, username, password) = getFirstTeam()
            coEvery { teamCall.getTeams(username = any(), password = any()) } throws Exception()

            shouldThrowExactly<Exception> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            coVerify(exactly = 1) { teamCall.getTeams(username = username, password = password) }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception when error occurs while statistic call`() = runBlocking {
            val (teamId, _, username, password) = getFirstTeam()
            coEvery { teamCall.getTeams(username = any(), password = any()) } returns listOf(getFirstTeam())
            coEvery { statisticCall.incrementStatistic(teamId = any(), bankId = any()) } throws Exception()

            shouldThrowExactly<Exception> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            coVerifySequence {
                teamCall.getTeams(username = username, password = password)
                statisticCall.incrementStatistic(teamId = teamId)
            }
        }
    }
}
