package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Team
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.getExampleTeam
import de.hennihaus.plugins.initKoin
import io.kotest.matchers.collections.shouldNotBeEmpty
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

@Disabled(value = "until dev cluster is available")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeamCallServiceIntegrationTest : KoinTest {

    private val classUnderTest: TeamCallService by inject()

    @JvmField
    @RegisterExtension
    @Suppress("unused")
    val koinTestInstance = KoinTestExtension.create {
        initKoin()
    }

    @AfterAll
    fun cleanUp() = stopKoin()

    @Nested
    inner class GetTeams {
        @Test
        fun `should return at least one team by username and password`() = runBlocking<Unit> {
            val (_, _, username, password) = getExampleTeam()

            val result: List<Team> = classUnderTest.getTeams(
                username = username,
                password = password,
            )

            result.shouldNotBeEmpty()
        }
    }
}
