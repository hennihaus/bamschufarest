package de.hennihaus.objectmothers

import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.DEFAULT_PASSWORD
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.FIRST_TEAM_USERNAME
import de.hennihaus.models.RatingLevel
import de.hennihaus.models.generated.Rating
import de.hennihaus.routes.resources.RatingResource

object RatingObjectMother {

    const val DEFAULT_RATING_LEVEL = "A"
    const val DEFAULT_SOCIAL_SECURITY_NUMBER = "123"
    const val DEFAULT_DELAY_IN_MILLISECONDS = 0L

    fun getBestRating(
        score: Int = RatingLevel.valueOf(value = DEFAULT_RATING_LEVEL).maxScore,
        failureRiskInPercent: Double = RatingLevel.valueOf(value = DEFAULT_RATING_LEVEL).failureRiskInPercent,
    ) = Rating(
        score = score,
        failureRiskInPercent = failureRiskInPercent,
    )

    fun getMinValidRatingResource(
        socialSecurityNumber: String? = DEFAULT_SOCIAL_SECURITY_NUMBER,
        ratingLevel: String? = RatingLevel.valueOf(value = DEFAULT_RATING_LEVEL).name,
        delayInMilliseconds: Long? = DEFAULT_DELAY_IN_MILLISECONDS,
        username: String? = FIRST_TEAM_USERNAME,
        password: String? = DEFAULT_PASSWORD,
    ) = RatingResource(
        socialSecurityNumber = socialSecurityNumber,
        ratingLevel = ratingLevel,
        delayInMilliseconds = delayInMilliseconds,
        username = username,
        password = password,
    )
}
