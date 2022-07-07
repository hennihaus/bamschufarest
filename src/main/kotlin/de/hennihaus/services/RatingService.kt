package de.hennihaus.services

import de.hennihaus.models.RatingLevel
import de.hennihaus.models.generated.Rating
import kotlinx.coroutines.delay
import org.koin.core.annotation.Single

@Single
class RatingService {

    suspend fun calculateRating(ratingLevel: String, delayInMilliseconds: Long?): Rating {
        delay(timeMillis = delayInMilliseconds ?: ZERO_DELAY)

        return RatingLevel.valueOf(value = ratingLevel.uppercase()).let {
            Rating(
                score = (it.minScore..it.maxScore).random(),
                failureRiskInPercent = it.failureRiskInPercent,
            )
        }
    }

    companion object {
        private const val ZERO_DELAY = 0L
    }
}
