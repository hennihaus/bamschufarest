package de.hennihaus.utils

import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ValidationUtilsTest {

    data class TestClass(val value: String)

    val validationUnderTest = Validation<TestClass> {
        TestClass::value {
            maxLength(length = 0)
        }
    }

    @Nested
    inner class ValidateAndThrowOnFailure {
        @Test
        fun `should not throw an exception when value is valid`() {
            val value = TestClass(value = "")

            shouldNotThrowAny {
                validationUnderTest.validateAndThrowOnFailure(value = value)
            }
        }

        @Test
        fun `should throw exception and correct message when value is invalid`() {
            val value = TestClass(value = "invalid")

            val result: ValidationException = shouldThrowExactly {
                validationUnderTest.validateAndThrowOnFailure(
                    value = value,
                )
            }

            result shouldHaveMessage "[value must have at most 0 characters]"
        }
    }
}
