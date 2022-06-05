package de.hennihaus.objectmothers

import de.hennihaus.models.Rating
import de.hennihaus.models.RatingLevel

object RatingObjectMother {

    fun getBestRating(
        score: Int = RatingLevel.A.maxScore,
        failureRiskInPercent: Double = RatingLevel.A.failureRiskInPercent
    ) = Rating(
        score = score,
        failureRiskInPercent = failureRiskInPercent
    )
}
