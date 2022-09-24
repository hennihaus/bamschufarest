package de.hennihaus.services

import de.hennihaus.bamdatamodel.objectmothers.BankObjectMother.SCHUFA_BANK_UUID
import de.hennihaus.bamdatamodel.objectmothers.BankObjectMother.getSchufaBank
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.getFirstTeam
import de.hennihaus.services.TrackingService.Companion.TEAM_NOT_FOUND_MESSAGE
import de.hennihaus.services.callservices.BankCallService
import de.hennihaus.services.callservices.StatisticCallService
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.server.plugins.NotFoundException
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

class TrackingServiceTest {

    private val bankId = UUID.fromString(SCHUFA_BANK_UUID)
    private val bankCall = mockk<BankCallService>()
    private val statisticCall = mockk<StatisticCallService>()

    private val classUnderTest = TrackingService(
        bankId = "$bankId",
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
            coEvery { bankCall.getBankById(id = any()) } returns getSchufaBank()
            coEvery { statisticCall.incrementStatistic(teamId = any(), bankId = any()) } returns mockk()

            classUnderTest.trackRequest(
                username = username,
                password = password,
            )

            coVerifySequence {
                bankCall.getBankById(
                    id = bankId
                )
                statisticCall.incrementStatistic(
                    teamId = teamId,
                    bankId = bankId,
                )
            }
        }

        @Test
        fun `should throw an exception and not increment statistic when bank has zero teams`() = runBlocking {
            val (_, username, password) = getFirstTeam()
            coEvery { bankCall.getBankById(id = any()) } returns getSchufaBank(teams = emptyList())

            val result = shouldThrowExactly<NotFoundException> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            result shouldHaveMessage TEAM_NOT_FOUND_MESSAGE
            coVerify(exactly = 1) { bankCall.getBankById(id = bankId) }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception and not increment statistic when username is unknown`() = runBlocking {
            val username = "unknown"
            val password = getFirstTeam().password
            coEvery { bankCall.getBankById(id = any()) } returns getSchufaBank(teams = emptyList())

            val result = shouldThrowExactly<NotFoundException> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            result shouldHaveMessage TEAM_NOT_FOUND_MESSAGE
            coVerify(exactly = 1) { bankCall.getBankById(id = bankId) }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception and not increment statistic when password is unknown`() = runBlocking {
            val username = getFirstTeam().username
            val password = "unknown"
            coEvery { bankCall.getBankById(id = any()) } returns getSchufaBank(teams = emptyList())

            val result = shouldThrowExactly<NotFoundException> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            result shouldHaveMessage TEAM_NOT_FOUND_MESSAGE
            coVerify(exactly = 1) { bankCall.getBankById(id = bankId) }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception when error occurs while bank call`() = runBlocking {
            val (_, username, password) = getFirstTeam()
            coEvery { bankCall.getBankById(id = any()) } throws Exception()

            shouldThrowExactly<Exception> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            coVerify(exactly = 1) { bankCall.getBankById(id = bankId) }
            coVerify(exactly = 0) { statisticCall.incrementStatistic(teamId = any(), bankId = any()) }
        }

        @Test
        fun `should throw an exception when error occurs while statistic call`() = runBlocking {
            val (teamId, username, password) = getFirstTeam()
            coEvery { bankCall.getBankById(id = any()) } returns getSchufaBank()
            coEvery { statisticCall.incrementStatistic(teamId = any(), bankId = any()) } throws Exception()

            shouldThrowExactly<Exception> {
                classUnderTest.trackRequest(
                    username = username,
                    password = password,
                )
            }

            coVerifySequence {
                bankCall.getBankById(
                    id = bankId
                )
                statisticCall.incrementStatistic(
                    teamId = teamId,
                    bankId = bankId,
                )
            }
        }
    }
}
