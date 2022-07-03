package de.hennihaus.services.resourceservices

import de.hennihaus.models.RatingLevel
import de.hennihaus.objectmothers.RatingObjectMother.getMinValidRatingResource
import de.hennihaus.plugins.ValidationException
import de.hennihaus.routes.resources.RatingResource
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RatingResourceServiceTest {

    private val classUnderTest = RatingResourceService()

    @Nested
    inner class Validate {

        @BeforeEach
        fun init() {
            mockkObject(RatingResourceServiceTest)
        }

        @Test
        fun `should execute test operation when rating resource is valid and ratingLevel = A`() = runBlocking {
            val resource = getMinValidRatingResource(ratingLevel = "${RatingLevel.A}")
            every { testOperation(ratingResource = any()) } returns Unit

            classUnderTest.validate(resource = resource) {
                testOperation(ratingResource = it)
            }

            verify(exactly = 1) {
                testOperation(
                    ratingResource = getMinValidRatingResource(
                        ratingLevel = "${RatingLevel.A}",
                    ),
                )
            }
        }

        @Test
        fun `should execute test operation when rating resource is valid and ratingLevel = a`() = runBlocking {
            val resource = getMinValidRatingResource(ratingLevel = "${RatingLevel.A}".lowercase())
            every { testOperation(ratingResource = any()) } returns Unit

            classUnderTest.validate(resource = resource) {
                testOperation(ratingResource = it)
            }

            verify(exactly = 1) {
                testOperation(
                    ratingResource = getMinValidRatingResource(
                        ratingLevel = "${RatingLevel.A}".lowercase(),
                    ),
                )
            }
        }

        @Test
        fun `should throw exception and not execute test operation when socialSecurityNumber = null`() = runBlocking {
            val resource = getMinValidRatingResource(socialSecurityNumber = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[socialSecurityNumber is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when socialSecurityNumber = empty`() = runBlocking {
            val resource = getMinValidRatingResource(socialSecurityNumber = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[socialSecurityNumber must have at least 1 characters]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when ratingLevel = null`() = runBlocking {
            val resource = getMinValidRatingResource(ratingLevel = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[ratingLevel is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when ratingLevel = Z`() = runBlocking {
            val resource = getMinValidRatingResource(ratingLevel = "Z")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(ratingResource = it)
                }
            }

            @Suppress("MaxLineLength")
            result shouldHaveMessage "[ratingLevel must be one of: 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'N', 'O', 'P', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'n', 'o', 'p']"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when delayInMilliseconds = null`() = runBlocking {
            val resource = getMinValidRatingResource(delayInMilliseconds = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[delayInMilliseconds is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when username = null`() = runBlocking {
            val resource = getMinValidRatingResource(username = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[username is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when username = empty`() = runBlocking {
            val resource = getMinValidRatingResource(username = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[username must have at least 1 characters]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when password = null`() = runBlocking {
            val resource = getMinValidRatingResource(password = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[password is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when password = empty`() = runBlocking {
            val resource = getMinValidRatingResource(password = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate(resource = resource) {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[password must have at least 1 characters]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }
    }

    companion object {
        fun testOperation(ratingResource: RatingResource) {
            println(ratingResource)
        }
    }
}
