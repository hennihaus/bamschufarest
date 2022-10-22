package de.hennihaus.services.validationservices

import de.hennihaus.plugins.RequestValidationException
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ValidationServiceTest {

    data class TestResource(val firstValue: String, val secondValue: String)

    val classUnderTest = object : ValidationService<TestResource> {
        override suspend fun urlValidation(resource: TestResource): Validation<TestResource> = Validation {
            TestResource::firstValue {
                maxLength(length = 0)
            }
            TestResource::secondValue {
                maxLength(length = 0)
            }
        }
    }

    @Nested
    inner class ValidateUrl {
        @Test
        fun `should not throw an exception when resource is valid`() = runBlocking {
            val resource = TestResource(
                firstValue = "",
                secondValue = "",
            )

            shouldNotThrowAny {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }
        }

        @Test
        fun `should throw an exception with two reasons when all values are invalid`() = runBlocking {
            val resource = TestResource(
                firstValue = "invalid",
                secondValue = "invalid",
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "firstValue must have at most 0 characters",
                    "secondValue must have at most 0 characters",
                ),
            )
        }
    }
}
