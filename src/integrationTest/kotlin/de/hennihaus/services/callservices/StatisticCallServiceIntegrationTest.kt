package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Statistic
import de.hennihaus.bamdatamodel.objectmothers.StatisticObjectMother.getFirstTeamAsyncBankStatistic
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother
import de.hennihaus.configurations.Configuration.BANK_UUID
import de.hennihaus.plugins.initKoin
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import java.util.UUID

@Disabled(value = "until dev cluster is available")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatisticCallServiceIntegrationTest : KoinTest {

    private val classUnderTest: StatisticCallService by inject()

    @JvmField
    @RegisterExtension
    @Suppress("unused")
    val koinTestInstance = KoinTestExtension.create {
        initKoin()
    }

    @AfterAll
    fun cleanUp() = stopKoin()

    @Nested
    inner class IncrementStatistic {
        @Test
        fun `should increment and return a statistic with requests greater zero`() = runBlocking<Unit> {
            val bankId = UUID.fromString(getKoin().getProperty(key = BANK_UUID))
            val teamId = TeamObjectMother.getExampleTeam().uuid

            val result: Statistic = classUnderTest.incrementStatistic(
                bankId = bankId,
                teamId = teamId,
            )

            result.shouldBeEqualToIgnoringFields(
                other = getFirstTeamAsyncBankStatistic(
                    bankId = bankId,
                    teamId = teamId,
                ),
                property = Statistic::requestsCount,
            )
            result.requestsCount!! shouldBeGreaterThanOrEqual 1L
        }
    }
}
