package de.hennihaus.objectmothers

import de.hennihaus.models.RatingLevel
import de.hennihaus.models.generated.Rating

object RatingObjectMother {

    fun getBestRating(
        score: Int = RatingLevel.A.maxScore,
        failureRiskInPercent: Double = RatingLevel.A.failureRiskInPercent
    ) = Rating(
        score = score,
        failureRiskInPercent = failureRiskInPercent
    )
}
