package de.hennihaus.services

import de.hennihaus.models.RatingLevel
import de.hennihaus.models.generated.Rating
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class RatingServiceTest {

    private val classUnderTest = RatingServiceImpl()

    @Nested
    inner class CalculateRating {
        @Test
        fun `should return correct rating when ratingLevel = A and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.A}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 0.77
            result.score shouldBeInRange 9858..9999
        }

        @Test
        fun `should return correct rating when ratingLevel = a and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.A}".lowercase()
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 0.77
            result.score shouldBeInRange 9858..9999
        }

        @Test
        fun `should return most bad rating when ratingLevel = null and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = null
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 98.07
            result.score shouldBeInRange 1..785
        }

        @Test
        fun `should delay at most 1ms when ratingLevel = A and delayInMilliseconds = null`() = runBlocking<Unit> {
            val ratingLevel = "${RatingLevel.A}"
            val delayInMilliseconds = null

            val time = measureTimeMillis {
                classUnderTest.calculateRating(
                    ratingLevel = ratingLevel,
                    delayInMilliseconds = delayInMilliseconds,
                )
            }

            time shouldBeLessThanOrEqual 1L
        }

        @Test
        fun `should delay at least 250ms when ratingLevel = A and delayInMilliseconds = 250`() = runBlocking<Unit> {
            val ratingLevel = "${RatingLevel.A}"
            val delayInMilliseconds = 250L

            val time = measureTimeMillis {
                classUnderTest.calculateRating(
                    ratingLevel = ratingLevel,
                    delayInMilliseconds = delayInMilliseconds,
                )
            }

            time shouldBeGreaterThanOrEqual delayInMilliseconds
        }

        @Test
        fun `should return correct rating when ratingLevel = B and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.B}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 1.79
            result.score shouldBeInRange 9752..9857
        }

        @Test
        fun `should return correct rating when ratingLevel = C and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.C}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 2.88
            result.score shouldBeInRange 9678..9751
        }

        @Test
        fun `should return correct rating when ratingLevel = D and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.D}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 3.82
            result.score shouldBeInRange 9580..9677
        }

        @Test
        fun `should return correct rating when ratingLevel = E and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.E}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 5.06
            result.score shouldBeInRange 9435..9579
        }

        @Test
        fun `should return correct rating when ratingLevel = F and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.F}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds
            )

            result.failureRiskInPercent shouldBe 7.06
            result.score shouldBeInRange 9196..9434
        }

        @Test
        fun `should return correct rating when ratingLevel = G and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.G}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 10.25
            result.score shouldBeInRange 8712..9195
        }

        @Test
        fun `should return correct rating when ratingLevel = H and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.H}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 16.01
            result.score shouldBeInRange 8140..8711
        }

        @Test
        fun `should return correct rating when ratingLevel = I and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.I}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 21.14
            result.score shouldBeInRange 7657..8139
        }

        @Test
        fun `should return correct rating when ratingLevel = J and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.J}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 24.69
            result.score shouldBeInRange 7247..7656
        }

        @Test
        fun `should return correct rating when ratingLevel = K and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.K}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 28.70
            result.score shouldBeInRange 6545..7246
        }

        @Test
        fun `should return correct rating when ratingLevel = L and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.L}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 39.86
            result.score shouldBeInRange 4612..6544
        }

        @Test
        fun `should return correct rating when ratingLevel = N and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.N}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 52.34
            result.score shouldBeInRange 3799..4611
        }

        @Test
        fun `should return correct rating when ratingLevel = O and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.O}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 79.38
            result.score shouldBeInRange 786..3798
        }

        @Test
        fun `should return correct rating when ratingLevel = P and delayInMilliseconds = 0`() = runBlocking {
            val ratingLevel = "${RatingLevel.P}"
            val delayInMilliseconds = 0L

            val result: Rating = classUnderTest.calculateRating(
                ratingLevel = ratingLevel,
                delayInMilliseconds = delayInMilliseconds,
            )

            result.failureRiskInPercent shouldBe 98.07
            result.score shouldBeInRange 1..785
        }
    }
}
