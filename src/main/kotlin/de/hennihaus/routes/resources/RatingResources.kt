package de.hennihaus.routes.resources

import de.hennihaus.models.RatingLevel
import de.hennihaus.utils.validateAndThrowOnFailure
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import io.konform.validation.jsonschema.minLength
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

object RatingPaths {
    const val RATINGS_PATH = "/ratings"
    const val SCORE_PATH = "/score"
}

@Serializable
@Resource(RatingPaths.RATINGS_PATH)
class Ratings {

    @Serializable
    @Resource(RatingPaths.SCORE_PATH)
    data class Score(
        val parent: Ratings = Ratings(),
        val socialSecurityNumber: String? = null,
        val ratingLevel: String? = null,
        val delayInMilliseconds: Long? = null,
        val username: String? = null,
        val password: String? = null
    )
}

inline fun Ratings.Score.validate(body: (Ratings.Score) -> Unit) {
    val validation = Validation<Ratings.Score> {
        Ratings.Score::socialSecurityNumber required {}
        Ratings.Score::ratingLevel required {
            enum<RatingLevel>()
        }
        Ratings.Score::delayInMilliseconds required {}
        Ratings.Score::username required {
            minLength(length = 1)
        }
        Ratings.Score::password required {
            minLength(length = 1)
        }
    }
    validation.validateAndThrowOnFailure(value = this).also {
        body(this)
    }
}
