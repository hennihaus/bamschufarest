package de.hennihaus.services

import de.hennihaus.models.generated.Rating

interface RatingService {
    suspend fun calculateRating(ratingLevel: String?, delayInMilliseconds: Long?): Rating
}
