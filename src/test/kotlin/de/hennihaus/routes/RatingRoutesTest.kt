package de.hennihaus.routes

import de.hennihaus.models.generated.Error
import de.hennihaus.models.generated.Rating
import de.hennihaus.objectmothers.ErrorObjectMother.getInvalidRequestError
import de.hennihaus.objectmothers.ErrorObjectMother.getNotFoundError
import de.hennihaus.objectmothers.RatingObjectMother.getBestRating
import de.hennihaus.objectmothers.ScoreObjectMother.getMinValidRequestScore
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

    private val ratingService = mockk<RatingService>()
    private val trackingService = mockk<TrackingService>()

    private val mockModule = module {
        single { ratingService }
        single { trackingService }
    }

    @BeforeEach
    fun init() = clearAllMocks()

    @AfterEach
    fun tearDown() = stopKoin()

    @Nested
    inner class GetRatingScore {

        @BeforeEach
        fun init() {
            // default behavior
            coEvery { trackingService.trackRequest(username = any(), password = any()) } returns Unit
            coEvery {
                ratingService.calculateScore(
                    ratingLevel = any(),
                    delayInMilliseconds = any(),
                )
            } returns getBestRating()
        }

        @Test
        fun `should return 200 and a rating score`() = testApplicationWith(mockModule) {
            val (
                _,
                socialSecurityNumber,
                ratingLevel,
                delayInMilliseconds,
                username,
                password
            ) = getMinValidRequestScore()

            val response = testClient.get(
                urlString = """
                    /ratings/score
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
                ratingService.calculateScore(ratingLevel = ratingLevel, delayInMilliseconds = delayInMilliseconds)
                trackingService.trackRequest(username = username, password = password)
            }
        }

        @Test
        fun `should return 400 and error when request (password) was invalid`() = testApplicationWith(mockModule) {
            val (
                _,
                socialSecurityNumber,
                ratingLevel,
                delayInMilliseconds,
                username
            ) = getMinValidRequestScore()
            val password = ""

            val response = testClient.get(
                urlString = """
                    /ratings/score
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
                    property = Error::message,
                )
                it.dateTime.shouldBeEqualToIgnoringFields(
                    other = getInvalidRequestError().dateTime,
                    property = LocalDateTime::second,
                )
            }
            coVerify(exactly = 0) { ratingService.calculateScore(ratingLevel = any(), delayInMilliseconds = any()) }
            coVerify(exactly = 0) { trackingService.trackRequest(username = any(), password = any()) }
        }

        @Test
        fun `should return 404 and error when NotFoundException is thrown`() = testApplicationWith(mockModule) {
            val (
                _,
                socialSecurityNumber,
                ratingLevel,
                delayInMilliseconds,
                username,
                password
            ) = getMinValidRequestScore()
            coEvery { trackingService.trackRequest(username = any(), password = any()) } throws NotFoundException(
                message = ErrorMessage.NOT_FOUND_MESSAGE
            )

            val response = testClient.get(
                urlString = """
                    /ratings/score
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
                    property = Error::message,
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
                _,
                socialSecurityNumber,
                ratingLevel,
                delayInMilliseconds,
                username,
                password
            ) = getMinValidRequestScore()
            coEvery { trackingService.trackRequest(username = any(), password = any()) } throws IllegalStateException()

            val response = testClient.get(
                urlString = """
                    /ratings/score
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
                    property = Error::message,
                    others = arrayOf(Error::dateTime),
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
