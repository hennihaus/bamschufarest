package de.hennihaus.services.callservices

import de.hennihaus.models.Group
import de.hennihaus.objectmothers.ConfigurationObjectMother.getConfigBackendConfiguration
import de.hennihaus.objectmothers.GroupObjectMother.getFirstGroup
import de.hennihaus.objectmothers.GroupObjectMother.getSecondGroup
import de.hennihaus.objectmothers.GroupObjectMother.getThirdGroup
import de.hennihaus.services.callservices.resources.GroupPaths.GROUPS_PATH
import de.hennihaus.testutils.MockEngineBuilder
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
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

class GroupCallServiceTest {

    private lateinit var classUnderTest: GroupCallService

    @Nested
    inner class GetAllGroups {
        @Test
        fun `should return a list of groups and build call correctly`() = runBlocking {
            val config = getConfigBackendConfiguration()
            val engine = MockEngineBuilder.getMockEngine(
                content = Json.encodeToString(
                    value = listOf(
                        getFirstGroup(),
                        getSecondGroup(),
                        getThirdGroup(),
                    ),
                ),
                assertions = {
                    it.method shouldBe HttpMethod.Get
                    it.url shouldBe Url(
                        urlString = "${config.protocol}://${config.host}:${config.port}$GROUPS_PATH"
                    )
                    it.headers[HttpHeaders.Accept] shouldBe "${ContentType.Application.Json}"
                }
            )
            classUnderTest = GroupCallService(
                engine = engine,
                config = config,
            )

            val result: List<Group> = classUnderTest.getAllGroups()

            result shouldBe listOf(
                getFirstGroup(),
                getSecondGroup(),
                getThirdGroup(),
            )
        }

        @Test
        fun `should return an empty list when no groups available`() = runBlocking<Unit> {
            val engine = MockEngineBuilder.getMockEngine(
                content = Json.encodeToString(
                    value = emptyList<Group>(),
                ),
            )
            classUnderTest = GroupCallService(
                engine = engine,
                config = getConfigBackendConfiguration(),
            )

            val result: List<Group> = classUnderTest.getAllGroups()

            result.shouldBeEmpty()
        }

        @Test
        fun `should throw an exception and request three times when error occurs`() = runBlocking {
            var counter = 0
            val engine = MockEngineBuilder.getMockEngine(
                status = HttpStatusCode.InternalServerError,
                assertions = { counter++ },
            )
            classUnderTest = GroupCallService(
                engine = engine,
                config = getConfigBackendConfiguration(maxRetries = 2),
            )

            shouldThrowExactly<ServerResponseException> {
                classUnderTest.getAllGroups()
            }

            counter shouldBe 3
        }
    }

    @Nested
    inner class UpdateGroup {
        @Test
        fun `should return an updated group and build call correctly`() = runBlocking {
            val group = getFirstGroup()
            val config = getConfigBackendConfiguration()
            val engine = MockEngineBuilder.getMockEngine(
                content = Json.encodeToString(
                    value = group,
                ),
                assertions = {
                    it.method shouldBe HttpMethod.Put
                    it.url shouldBe Url(
                        urlString = "${config.protocol}://${config.host}:${config.port}$GROUPS_PATH/${group.id}"
                    )
                    it.headers[HttpHeaders.Accept] shouldBe "${ContentType.Application.Json}"
                    // it.body shouldBe group.toBody()
                },
            )
            classUnderTest = GroupCallService(
                engine = engine,
                config = config,
            )

            val result: Group = classUnderTest.updateGroup(
                id = group.id,
                group = group,
            )

            result shouldBe getFirstGroup()
        }

        @Test
        fun `should throw an exception and request three times when error occurs`() = runBlocking {
            var counter = 0
            val group = getFirstGroup()
            val engine = MockEngineBuilder.getMockEngine(
                status = HttpStatusCode.InternalServerError,
                assertions = { counter++ },
            )
            classUnderTest = GroupCallService(
                engine = engine,
                config = getConfigBackendConfiguration(maxRetries = 2),
            )

            shouldThrowExactly<ServerResponseException> {
                classUnderTest.updateGroup(
                    id = group.id,
                    group = group,
                )
            }

            counter shouldBe 3
        }
    }
}
