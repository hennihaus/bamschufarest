package de.hennihaus.routes

import de.hennihaus.models.generated.Error
import de.hennihaus.models.generated.Rating
import de.hennihaus.objectmothers.ErrorObjectMother.DEFAULT_INVALID_REQUEST_ERROR_MESSAGE
import de.hennihaus.objectmothers.ErrorObjectMother.DEFAULT_NOT_FOUND_ERROR_MESSAGE
import de.hennihaus.objectmothers.ErrorObjectMother.getInternalServerError
import de.hennihaus.objectmothers.ErrorObjectMother.getInvalidRequestError
import de.hennihaus.objectmothers.ErrorObjectMother.getNotFoundError
import de.hennihaus.objectmothers.RatingObjectMother.getBestRating
import de.hennihaus.objectmothers.RatingObjectMother.getMinValidRatingResource
import de.hennihaus.plugins.ValidationException
import de.hennihaus.services.RatingService
import de.hennihaus.services.TrackingService
import de.hennihaus.services.resourceservices.RatingResourceService
import de.hennihaus.testutils.KtorTestBuilder.testApplicationWith
import de.hennihaus.testutils.testClient
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.NotFoundException
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RatingRoutesTest {

    private val ratingResource = mockk<RatingResourceService>()
    private val rating = mockk<RatingService>()
    private val tracking = mockk<TrackingService>()

    private val mockModule = module {
        single { ratingResource }
        single { rating }
        single { tracking }
    }

    @BeforeEach
    fun init() = clearAllMocks()

    @AfterEach
    fun tearDown() = stopKoin()

    @Nested
    inner class GetRating {

        @BeforeEach
        fun init() {
            // default behavior
            coEvery { ratingResource.validate(resource = any()) } returns getMinValidRatingResource()
            coEvery { tracking.trackRequest(username = any(), password = any()) } returns Unit
            coEvery {
                rating.calculateRating(
                    ratingLevel = any(),
                    delayInMilliseconds = any(),
                )
            } returns getBestRating()
        }

        @Test
        fun `should return 200 and a rating`() = testApplicationWith(mockModule) {
            val (
                socialSecurityNumber,
                ratingLevel,
                delayInMilliseconds,
                username,
                password,
            ) = getMinValidRatingResource()

            val response = testClient.get(
                urlString = """
                    /rating
                    ?socialSecurityNumber=$socialSecurityNumber
                    &ratingLevel=$ratingLevel
                    &delayInMilliseconds=$delayInMilliseconds
                    &username=$username
                    &password=$password
                """.trimIndent().replace(oldValue = "\n", newValue = ""),
            )

            response shouldHaveStatus HttpStatusCode.OK
            response.body<Rating>() shouldBe getBestRating()
            coVerifySequence {
                ratingResource.validate(
                    resource = getMinValidRatingResource(),
                )
                rating.calculateRating(
                    ratingLevel = ratingLevel!!,
                    delayInMilliseconds = delayInMilliseconds,
                )
                tracking.trackRequest(
                    username = username!!,
                    password = password!!,
                )
            }
        }

        @Test
        fun `should return 400 and error when ratingLevel missing and invalid`() = testApplicationWith(mockModule) {
            val (
                socialSecurityNumber,
                _,
                delayInMilliseconds,
                username,
                password,
            ) = getMinValidRatingResource()
            coEvery { ratingResource.validate(resource = any()) } throws ValidationException(
                message = DEFAULT_INVALID_REQUEST_ERROR_MESSAGE,
            )

            val response = testClient.get(
                urlString = """
                    /rating
                    ?socialSecurityNumber=$socialSecurityNumber
                    &delayInMilliseconds=$delayInMilliseconds
                    &username=$username
                    &password=$password
                """.trimIndent().replace("\n", ""),
            )

            response shouldHaveStatus HttpStatusCode.BadRequest
            response.body<Error>().shouldBeEqualToIgnoringFields(
                other = getInvalidRequestError(),
                property = Error::dateTime,
            )
            coVerify(exactly = 1) {
                ratingResource.validate(
                    resource = getMinValidRatingResource(
                        ratingLevel = null,
                    ),
                )
            }
            coVerify(exactly = 0) {
                rating.calculateRating(
                    ratingLevel = any(),
                    delayInMilliseconds = any(),
                )
            }
            coVerify(exactly = 0) {
                tracking.trackRequest(
                    username = any(),
                    password = any(),
                )
            }
        }

        @Test
        fun `should return 404 and error when NotFoundException is thrown`() = testApplicationWith(mockModule) {
            val (
                socialSecurityNumber,
                ratingLevel,
                delayInMilliseconds,
                username,
                password,
            ) = getMinValidRatingResource()
            coEvery { tracking.trackRequest(username = any(), password = any()) } throws NotFoundException(
                message = DEFAULT_NOT_FOUND_ERROR_MESSAGE,
            )

            val response = testClient.get(
                urlString = """
                    /rating
                    ?socialSecurityNumber=$socialSecurityNumber
                    &ratingLevel=$ratingLevel
                    &delayInMilliseconds=$delayInMilliseconds
                    &username=$username
                    &password=$password
                """.trimIndent().replace("\n", ""),
            )

            response shouldHaveStatus HttpStatusCode.NotFound
            response.body<Error>().shouldBeEqualToIgnoringFields(
                other = getNotFoundError(),
                property = Error::dateTime,
            )
        }

        @Test
        fun `should return 500 and error when IllegalStateException is thrown`() = testApplicationWith(mockModule) {
            val (
                socialSecurityNumber,
                ratingLevel,
                delayInMilliseconds,
                username,
                password
            ) = getMinValidRatingResource()
            coEvery { tracking.trackRequest(username = any(), password = any()) } throws IllegalStateException()

            val response = testClient.get(
                urlString = """
                    /rating
                    ?socialSecurityNumber=$socialSecurityNumber
                    &ratingLevel=$ratingLevel
                    &delayInMilliseconds=$delayInMilliseconds
                    &username=$username
                    &password=$password
                """.trimIndent().replace("\n", ""),
            )

            response shouldHaveStatus HttpStatusCode.InternalServerError
            response.body<Error>().shouldBeEqualToIgnoringFields(
                other = getInternalServerError(),
                property = Error::dateTime,
            )
        }
    }
}
