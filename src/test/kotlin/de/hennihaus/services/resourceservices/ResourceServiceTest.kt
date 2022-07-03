package de.hennihaus.services.resourceservices

import de.hennihaus.plugins.ValidationException
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ResourceServiceTest {

    data class TestResource(val value: String)

    val classUnderTest = object : ResourceService<TestResource, Unit> {
        override val resourceValidation: Validation<TestResource> = Validation {
            TestResource::value {
                maxLength(length = 0)
            }
        }
    }

    @Nested
    inner class Validate {

        @BeforeEach
        fun init() {
            mockkObject(ResourceServiceTest)
        }

        @Test
        fun `should not throw an exception when value is valid`() = runBlocking {
            val resource = TestResource(value = "")

            shouldNotThrowAny {
                classUnderTest.validate(resource = resource) {
                    testOperation(resource = it)
                }
            }

            verify(exactly = 1) {
                testOperation(resource = resource)
            }
        }

        @Test
        fun `should throw exception and correct message when value is invalid`() = runBlocking {
            val resource = TestResource(value = "invalid")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(resource = it)
                }
            }

            result shouldHaveMessage "[value must have at most 0 characters]"
            verify(exactly = 0) {
                testOperation(resource = any())
            }
        }
    }

    companion object {
        fun testOperation(resource: TestResource) {
            println(resource)
        }
    }
}
