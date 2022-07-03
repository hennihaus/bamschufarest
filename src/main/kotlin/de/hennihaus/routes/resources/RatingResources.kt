package de.hennihaus.routes.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

object RatingPaths {
    const val RATING_PATH = "/rating"
}

@Serializable
@Resource(RatingPaths.RATING_PATH)
data class RatingResource(
    val socialSecurityNumber: String? = null,
    val ratingLevel: String? = null,
    val delayInMilliseconds: Long? = null,
    val username: String? = null,
    val password: String? = null,
)
