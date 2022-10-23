package de.hennihaus.services.validationservices

import de.hennihaus.models.RatingLevel
import de.hennihaus.objectmothers.RatingObjectMother.getMinValidRatingResource
import de.hennihaus.plugins.RequestValidationException
import de.hennihaus.services.validationservices.RatingValidationService.Companion.PASSWORD_MAX_LENGTH
import de.hennihaus.services.validationservices.RatingValidationService.Companion.PASSWORD_MIN_LENGTH
import de.hennihaus.services.validationservices.RatingValidationService.Companion.SOCIAL_SECURITY_NUMBER_MIN_LENGTH
import de.hennihaus.services.validationservices.RatingValidationService.Companion.USERNAME_MAX_LENGTH
import de.hennihaus.services.validationservices.RatingValidationService.Companion.USERNAME_MIN_LENGTH
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.single
import io.kotest.property.arbitrary.string
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RatingValidationServiceTest {

    private val classUnderTest = RatingValidationService()

    @Nested
    inner class ValidateUrl {
        @Test
        fun `should not throw an exception when resource is valid and ratingLevel is uppercase`() = runBlocking {
            val resource = getMinValidRatingResource(
                ratingLevel = "${RatingLevel.A}".uppercase(),
            )

            shouldNotThrowAny {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }
        }

        @Test
        fun `should not throw an exception when resource is valid and ratingLevel is lowercase`() = runBlocking {
            val resource = getMinValidRatingResource(
                ratingLevel = "${RatingLevel.A}".lowercase(),
            )

            shouldNotThrowAny {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }
        }

        @Test
        fun `should throw an exception with one reason when socialSecurityNumber is null`() = runBlocking {
            val resource = getMinValidRatingResource(
                socialSecurityNumber = null,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "socialSecurityNumber is required",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when socialSecurityNumber is too short`() = runBlocking {
            val socialSecurityNumber = Arb.string(size = SOCIAL_SECURITY_NUMBER_MIN_LENGTH.dec()).single()
            val resource = getMinValidRatingResource(
                socialSecurityNumber = socialSecurityNumber,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "socialSecurityNumber must have at least $SOCIAL_SECURITY_NUMBER_MIN_LENGTH characters",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when ratingLevel is null`() = runBlocking {
            val resource = getMinValidRatingResource(
                ratingLevel = null,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "ratingLevel is required",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when ratingLevel is M`() = runBlocking {
            val resource = getMinValidRatingResource(
                ratingLevel = "M",
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "ratingLevel must be one of: 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'N', 'O', 'P', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'n', 'o', 'p'",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when delayInMilliseconds is null`() = runBlocking {
            val resource = getMinValidRatingResource(
                delayInMilliseconds = null,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "delayInMilliseconds is required",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when delayInMilliseconds is no number`() = runBlocking {
            val resource = getMinValidRatingResource(
                delayInMilliseconds = "no number",
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "delayInMilliseconds must be a number",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when username is null`() = runBlocking {
            val resource = getMinValidRatingResource(
                username = null,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "username is required",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when username is too short`() = runBlocking {
            val username = Arb.string(size = USERNAME_MIN_LENGTH.dec()).single()
            val resource = getMinValidRatingResource(
                username = username,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "username must have at least $USERNAME_MIN_LENGTH characters",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when username is too long`() = runBlocking {
            val username = Arb.string(size = USERNAME_MAX_LENGTH.inc()).single()
            val resource = getMinValidRatingResource(
                username = username,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "username must have at most $USERNAME_MAX_LENGTH characters",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when password is null`() = runBlocking {
            val resource = getMinValidRatingResource(
                password = null,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "password is required",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when password is too short`() = runBlocking {
            val password = Arb.string(size = PASSWORD_MIN_LENGTH.dec()).single()
            val resource = getMinValidRatingResource(
                password = password,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "password must have at least $PASSWORD_MIN_LENGTH characters",
                ),
            )
        }

        @Test
        fun `should throw an exception with one reason when password is too long`() = runBlocking {
            val password = Arb.string(size = PASSWORD_MAX_LENGTH.inc()).single()
            val resource = getMinValidRatingResource(
                password = password,
            )

            val result = shouldThrowExactly<RequestValidationException> {
                classUnderTest.validateUrl(
                    resource = resource,
                )
            }

            result shouldBe RequestValidationException(
                reasons = listOf(
                    "password must have at most $PASSWORD_MAX_LENGTH characters",
                ),
            )
        }
    }
}
