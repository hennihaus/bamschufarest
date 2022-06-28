package de.hennihaus.routes

import de.hennihaus.models.generated.Error
import de.hennihaus.models.generated.Rating
import de.hennihaus.objectmothers.ErrorObjectMother.getInvalidRequestError
import de.hennihaus.objectmothers.ErrorObjectMother.getNotFoundError
import de.hennihaus.objectmothers.RatingObjectMother.getBestRating
import de.hennihaus.objectmothers.RatingObjectMother.getMinValidRatingResource
import de.hennihaus.plugins.ErrorMessage
import de.hennihaus.services.RatingService
import de.hennihaus.services.TrackingService
import de.hennihaus.testutils.KtorTestBuilder.testApplicationWith
import de.hennihaus.testutils.testClient
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.should
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
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RatingRoutesTest {

    private val rating = mockk<RatingService>()
    private val tracking = mockk<TrackingService>()

    private val mockModule = module {
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
                """.trimIndent().replace("\n", ""),
            )

            response shouldHaveStatus HttpStatusCode.OK
            response.body<Rating>() shouldBe getBestRating()
            coVerifySequence {
                rating.calculateRating(ratingLevel = ratingLevel, delayInMilliseconds = delayInMilliseconds)
                tracking.trackRequest(username = username, password = password)
            }
        }

        @Test
        fun `should return 400 and error when request (password) was invalid`() = testApplicationWith(mockModule) {
            val (
                socialSecurityNumber,
                ratingLevel,
                delayInMilliseconds,
                username,
            ) = getMinValidRatingResource()
            val password = ""

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

            response shouldHaveStatus HttpStatusCode.BadRequest
            response.body<Error>() should {
                it.shouldBeEqualToIgnoringFields(
                    other = getInvalidRequestError(),
                    property = Error::dateTime,
                )
                it.dateTime.shouldBeEqualToIgnoringFields(
                    other = getInvalidRequestError().dateTime,
                    property = LocalDateTime::second,
                )
            }
            coVerify(exactly = 0) { rating.calculateRating(ratingLevel = any(), delayInMilliseconds = any()) }
            coVerify(exactly = 0) { tracking.trackRequest(username = any(), password = any()) }
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
                message = ErrorMessage.NOT_FOUND_MESSAGE
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
            response.body<Error>() should {
                it.shouldBeEqualToIgnoringFields(
                    other = getNotFoundError(),
                    property = Error::dateTime,
                )
                it.dateTime.shouldBeEqualToIgnoringFields(
                    other = getNotFoundError().dateTime,
                    property = LocalDateTime::second,
                )
            }
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
            response.body<Error>() should {
                it.shouldBeEqualToIgnoringFields(
                    other = getNotFoundError(),
                    property = Error::dateTime,
                    others = arrayOf(Error::message),
                )
                it.message shouldBe "${IllegalStateException()}"
                it.dateTime.shouldBeEqualToIgnoringFields(
                    other = getNotFoundError().dateTime,
                    property = LocalDateTime::second,
                )
            }
        }
    }
}
