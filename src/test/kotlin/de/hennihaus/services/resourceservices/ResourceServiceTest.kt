package de.hennihaus.services.resourceservices

import de.hennihaus.plugins.ValidationException
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ResourceServiceTest {

    data class TestResource(val firstValue: String, val secondValue: String)

    val classUnderTest = object : ResourceService<TestResource> {
        override suspend fun resourceValidation(): Validation<TestResource> = Validation {
            TestResource::firstValue {
                maxLength(length = 0)
            }
            TestResource::secondValue {
                maxLength(length = 0)
            }
        }
    }

    @Nested
    inner class Validate {

        @Test
        fun `should return same resource when values are valid`() = runBlocking {
            val resource = TestResource(firstValue = "", secondValue = "")

            val result: TestResource = classUnderTest.validate(
                resource = resource,
            )

            result shouldBe resource
        }

        @Test
        fun `should throw exception and correct message when values are invalid`() = runBlocking {
            val resource = TestResource(firstValue = "invalid", secondValue = "invalid")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            result shouldHaveMessage """
                [firstValue must have at most 0 characters, secondValue must have at most 0 characters]
            """.trimIndent()
        }
    }
}
