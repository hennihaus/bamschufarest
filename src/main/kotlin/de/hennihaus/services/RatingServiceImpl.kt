package de.hennihaus.services

import de.hennihaus.models.Rating
import de.hennihaus.models.RatingLevel
import kotlinx.coroutines.delay
import org.koin.core.annotation.Single

@Single
class RatingServiceImpl : RatingService {

    override suspend fun calculateScore(ratingLevel: String?, delayInMilliseconds: Long?): Rating {
        delay(timeMillis = delayInMilliseconds ?: ZERO_DELAY)

        return RatingLevel.valueOf(value = ratingLevel?.uppercase() ?: RatingLevel.P.name).let {
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
