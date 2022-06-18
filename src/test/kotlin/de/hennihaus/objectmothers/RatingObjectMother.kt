package de.hennihaus.objectmothers

import de.hennihaus.models.RatingLevel
import de.hennihaus.models.generated.Rating
import de.hennihaus.routes.resources.RatingResource

object RatingObjectMother {

    fun getBestRating(
        score: Int = RatingLevel.A.maxScore,
        failureRiskInPercent: Double = RatingLevel.A.failureRiskInPercent,
    ) = Rating(
        score = score,
        failureRiskInPercent = failureRiskInPercent,
    )

    fun getMinValidRatingResource(
        socialSecurityNumber: String? = "123",
        ratingLevel: String? = "A",
        delayInMilliseconds: Long? = 0L,
        username: String? = "H",
        password: String? = "H",
    ) = RatingResource(
        socialSecurityNumber = socialSecurityNumber,
        ratingLevel = ratingLevel,
        delayInMilliseconds = delayInMilliseconds,
        username = username,
        password = password,
    )
}
