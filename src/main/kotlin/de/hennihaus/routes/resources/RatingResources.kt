package de.hennihaus.routes.resources

import de.hennihaus.models.RatingLevel
import de.hennihaus.utils.validateAndThrowOnFailure
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import io.konform.validation.jsonschema.minLength
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

inline fun RatingResource.validate(body: (RatingResource) -> Unit) {
    val validation = Validation<RatingResource> {
        RatingResource::socialSecurityNumber required {
            minLength(length = 1)
        }
        RatingResource::ratingLevel required {
            enum<RatingLevel>()
        }
        RatingResource::delayInMilliseconds required {}
        RatingResource::username required {
            minLength(length = 1)
        }
        RatingResource::password required {
            minLength(length = 1)
        }
    }
    validation.validateAndThrowOnFailure(value = this).also {
        body(this)
    }
}
