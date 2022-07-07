package de.hennihaus.services.resourceservices

import de.hennihaus.models.RatingLevel
import de.hennihaus.objectmothers.RatingObjectMother.getMinValidRatingResource
import de.hennihaus.plugins.ValidationException
import de.hennihaus.routes.resources.RatingResource
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RatingResourceServiceTest {

    private val classUnderTest = RatingResourceService()

    @Nested
    inner class Validate {

        @Test
        fun `should return same resource when valid and ratingLevel = A`() = runBlocking {
            val resource = getMinValidRatingResource(ratingLevel = "${RatingLevel.A}")

            val result: RatingResource = classUnderTest.validate(resource = resource)

            result shouldBe resource
        }

        @Test
        fun `should return same resource when valid and ratingLevel = a`() = runBlocking {
            val resource = getMinValidRatingResource(ratingLevel = "${RatingLevel.A}".lowercase())

            val result: RatingResource = classUnderTest.validate(resource = resource)

            result shouldBe resource
        }

        @Test
        fun `should throw exception when socialSecurityNumber = null`() = runBlocking {
            val resource = getMinValidRatingResource(socialSecurityNumber = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            result shouldHaveMessage "[socialSecurityNumber is required]"
        }

        @Test
        fun `should throw exception when socialSecurityNumber = empty`() = runBlocking {
            val resource = getMinValidRatingResource(socialSecurityNumber = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            result shouldHaveMessage "[socialSecurityNumber must have at least 1 characters]"
        }

        @Test
        fun `should throw exception when ratingLevel = null`() = runBlocking {
            val resource = getMinValidRatingResource(ratingLevel = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            result shouldHaveMessage "[ratingLevel is required]"
        }

        @Test
        fun `should throw exception when ratingLevel = M`() = runBlocking {
            val resource = getMinValidRatingResource(ratingLevel = "M")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            @Suppress("MaxLineLength")
            result shouldHaveMessage "[ratingLevel must be one of: 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'N', 'O', 'P', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'n', 'o', 'p']"
        }

        @Test
        fun `should throw exception when delayInMilliseconds = null`() = runBlocking {
            val resource = getMinValidRatingResource(delayInMilliseconds = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            result shouldHaveMessage "[delayInMilliseconds is required]"
        }

        @Test
        fun `should throw exception when username = null`() = runBlocking {
            val resource = getMinValidRatingResource(username = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            result shouldHaveMessage "[username is required]"
        }

        @Test
        fun `should throw exception when username = empty`() = runBlocking {
            val resource = getMinValidRatingResource(username = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            result shouldHaveMessage "[username must have at least 1 characters]"
        }

        @Test
        fun `should throw exception when password = null`() = runBlocking {
            val resource = getMinValidRatingResource(password = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            result shouldHaveMessage "[password is required]"
        }

        @Test
        fun `should throw exception when password = empty`() = runBlocking {
            val resource = getMinValidRatingResource(password = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource)
            }

            result shouldHaveMessage "[password must have at least 1 characters]"
        }
    }
}
