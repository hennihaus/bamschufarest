package de.hennihaus.routes.resources

import de.hennihaus.objectmothers.RatingObjectMother.getMinValidRatingResource
import de.hennihaus.utils.ValidationException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RatingResourcesTest {

    @BeforeEach
    fun init() = clearAllMocks()

    @Nested
    inner class Validate {

        @BeforeEach
        fun init() {
            mockkObject(RatingResourcesTest)

            every { testOperation(ratingResource = any()) } returns Unit
        }

        @Test
        fun `should execute test operation when rating is valid`() {
            val classUnderTest = getMinValidRatingResource()

            classUnderTest.validate { testOperation(ratingResource = it) }

            verify(exactly = 1) {
                testOperation(ratingResource = getMinValidRatingResource())
            }
        }

        @Test
        fun `should throw exception and not execute test operation when socialSecurityNumber = null`() {
            val classUnderTest = getMinValidRatingResource(socialSecurityNumber = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[socialSecurityNumber is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when ratingLevel = null`() {
            val classUnderTest = getMinValidRatingResource(ratingLevel = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[ratingLevel is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when ratingLevel = Z`() {
            val classUnderTest = getMinValidRatingResource(ratingLevel = "Z")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage """
                [ratingLevel must be one of: 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'N', 'O', 'P']
            """.trimIndent()
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when delayInMilliseconds = null`() {
            val classUnderTest = getMinValidRatingResource(delayInMilliseconds = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[delayInMilliseconds is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when username = null`() {
            val classUnderTest = getMinValidRatingResource(username = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[username is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when username = empty`() {
            val classUnderTest = getMinValidRatingResource(username = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[username must have at least 1 characters]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when password = null`() {
            val classUnderTest = getMinValidRatingResource(password = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(ratingResource = it)
                }
            }

            result shouldHaveMessage "[password is required]"
            verify(exactly = 0) { testOperation(ratingResource = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when password = empty`() {
            val classUnderTest = getMinValidRatingResource(password = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
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
