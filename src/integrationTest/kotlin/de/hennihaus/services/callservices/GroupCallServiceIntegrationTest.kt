package de.hennihaus.services.callservices

import de.hennihaus.models.Group
import de.hennihaus.objectmothers.GroupObjectMother
import de.hennihaus.plugins.initKoin
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
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
class GroupCallServiceIntegrationTest : KoinTest {

    private val classUnderTest: GroupCallService by inject()

    @JvmField
    @RegisterExtension
    @Suppress("unused")
    val koinTestInstance = KoinTestExtension.create {
        initKoin()
    }

    @AfterAll
    fun cleanUp() = stopKoin()

    @Nested
    inner class GetAllGroups {
        @Test
        fun `should return at least one group`() = runBlocking<Unit> {

            val result: List<Group> = classUnderTest.getAllGroups()

            result.shouldNotBeEmpty()
        }
    }

    @Nested
    inner class UpdateGroup {
        @Test
        fun `should return an updated group`() = runBlocking {
            val group = GroupObjectMother.getFirstGroup()

            val result: Group = classUnderTest.updateGroup(
                id = group.id,
                group = group,
            )

            result shouldBe group
        }
    }
}
