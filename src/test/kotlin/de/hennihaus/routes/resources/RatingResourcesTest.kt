package de.hennihaus.routes.resources

import de.hennihaus.objectmothers.ScoreObjectMother.getMinValidRequestScore
import de.hennihaus.utils.ValidationException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
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
    inner class ScoreValidate {

        @BeforeEach
        fun init() {
            mockkObject(RatingResourcesTest)

            every { testOperation(score = any()) } returns Unit
        }

        @Test
        fun `should execute test operation when score is valid`() {
            val classUnderTest = getMinValidRequestScore()

            classUnderTest.validate { testOperation(score = it) }

            verify(exactly = 1) {
                testOperation(score = withArg {
                    it.shouldBeEqualToIgnoringFields(
                        other = getMinValidRequestScore(),
                        property = Ratings.Score::parent
                    )
                })
            }
        }

        @Test
        fun `should throw exception and not execute test operation when socialSecurityNumber = null`() {
            val classUnderTest = getMinValidRequestScore(socialSecurityNumber = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(score = it)
                }
            }

            result shouldHaveMessage "[socialSecurityNumber is required]"
            verify(exactly = 0) { testOperation(score = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when ratingLevel = null`() {
            val classUnderTest = getMinValidRequestScore(ratingLevel = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(score = it)
                }
            }

            result shouldHaveMessage "[ratingLevel is required]"
            verify(exactly = 0) { testOperation(score = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when ratingLevel = Z`() {
            val classUnderTest = getMinValidRequestScore(ratingLevel = "Z")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(score = it)
                }
            }

            result shouldHaveMessage """
                [ratingLevel must be one of: 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'N', 'O', 'P']
            """.trimIndent()
            verify(exactly = 0) { testOperation(score = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when delayInMilliseconds = null`() {
            val classUnderTest = getMinValidRequestScore(delayInMilliseconds = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(score = it)
                }
            }

            result shouldHaveMessage "[delayInMilliseconds is required]"
            verify(exactly = 0) { testOperation(score = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when username = null`() {
            val classUnderTest = getMinValidRequestScore(username = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(score = it)
                }
            }

            result shouldHaveMessage "[username is required]"
            verify(exactly = 0) { testOperation(score = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when username = empty`() {
            val classUnderTest = getMinValidRequestScore(username = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(score = it)
                }
            }

            result shouldHaveMessage "[username must have at least 1 characters]"
            verify(exactly = 0) { testOperation(score = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when password = null`() {
            val classUnderTest = getMinValidRequestScore(password = null)

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(score = it)
                }
            }

            result shouldHaveMessage "[password is required]"
            verify(exactly = 0) { testOperation(score = any()) }
        }

        @Test
        fun `should throw exception and not execute test operation when password = empty`() {
            val classUnderTest = getMinValidRequestScore(password = "")

            val result: ValidationException = shouldThrowExactly {
                classUnderTest.validate {
                    testOperation(score = it)
                }
            }

            result shouldHaveMessage "[password must have at least 1 characters]"
            verify(exactly = 0) { testOperation(score = any()) }
        }
    }

    companion object {
        fun testOperation(score: Ratings.Score) { println(score) }
    }
}
