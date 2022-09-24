package de.hennihaus.services

import de.hennihaus.bamdatamodel.objectmothers.BankObjectMother.getSchufaBank
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.getFirstTeam
import de.hennihaus.objectmothers.ConfigurationObjectMother.getConfigBackendConfiguration
import de.hennihaus.services.TrackingService.Companion.TEAM_NOT_FOUND_MESSAGE
import de.hennihaus.services.callservices.BankCallService
import de.hennihaus.services.callservices.StatisticCallService
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

    private val bankCall = spyk(
        objToCopy = BankCallService(
            defaultBankId = "${getSchufaBank().uuid}",
            engine = CIO.create(),
            config = config,
        ),
    )
    private val statisticCall = spyk(
        objToCopy = StatisticCallService(
            defaultBankId = "${getSchufaBank().uuid}",
            engine = CIO.create(),
            config = config,
        )
    )

    private val classUnderTest = TrackingService(
        bankCall = bankCall,
        statisticCall = statisticCall,
    )

    @BeforeEach
    fun init() = clearAllMocks()

    @Nested
    inner class TrackRequest {
        @Test
        fun `should call increment statistic with teamId when username and password in bank teams`() = runBlocking {
            val (teamId, username, password) = getFirstTeam()
            coEvery { bankCall.getBankById() } returns getSchufaBank()
            coEvery { statisticCall.incrementStatistic(teamId = any(), bankId = any()) } returns mockk()

            classUnderTest.trackRequest(
                username = username,
                password = password,
            )

            coVerifySequence {
                bankCall.getBankById()
                statisticCall.incrementStatistic(teamId = teamId)
            }
        }

        @Test
        fun `should throw an exception and not increment statistic when bank has zero teams`() = runBlocking {
            val (_, username, password) = getFirstTeam()
            coEvery { bankCall.getBankById() } returns getSchufaBank(teams = emptyList())

            val result = shouldThrowExactly<NotFoundException> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            result shouldHaveMessage TEAM_NOT_FOUND_MESSAGE
            coVerify(exactly = 1) { bankCall.getBankById() }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception and not increment statistic when username is unknown`() = runBlocking {
            val username = "unknown"
            val password = getFirstTeam().password
            coEvery { bankCall.getBankById() } returns getSchufaBank()

            val result = shouldThrowExactly<NotFoundException> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            result shouldHaveMessage TEAM_NOT_FOUND_MESSAGE
            coVerify(exactly = 1) { bankCall.getBankById() }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception and not increment statistic when password is unknown`() = runBlocking {
            val username = getFirstTeam().username
            val password = "unknown"
            coEvery { bankCall.getBankById() } returns getSchufaBank()

            val result = shouldThrowExactly<NotFoundException> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            result shouldHaveMessage TEAM_NOT_FOUND_MESSAGE
            coVerify(exactly = 1) { bankCall.getBankById() }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception when error occurs while bank call`() = runBlocking {
            val (_, username, password) = getFirstTeam()
            coEvery { bankCall.getBankById() } throws Exception()

            shouldThrowExactly<Exception> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            coVerify(exactly = 1) { bankCall.getBankById() }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception when error occurs while statistic call`() = runBlocking {
            val (teamId, username, password) = getFirstTeam()
            coEvery { bankCall.getBankById() } returns getSchufaBank()
            coEvery { statisticCall.incrementStatistic(teamId = any(), bankId = any()) } throws Exception()

            shouldThrowExactly<Exception> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            coVerifySequence {
                bankCall.getBankById()
                statisticCall.incrementStatistic(teamId = teamId)
            }
        }
    }
}
