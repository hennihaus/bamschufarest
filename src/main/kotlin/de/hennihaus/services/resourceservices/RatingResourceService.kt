package de.hennihaus.services.resourceservices

import de.hennihaus.models.RatingLevel
import de.hennihaus.routes.resources.RatingResource
import io.konform.validation.Constraint
import io.konform.validation.Validation
import io.konform.validation.ValidationBuilder
import io.konform.validation.jsonschema.minLength
import org.koin.core.annotation.Single

@Single
class RatingResourceService : ResourceService<RatingResource> {

    override suspend fun resourceValidation(): Validation<RatingResource> = Validation {
        RatingResource::socialSecurityNumber required {
            minLength(length = SOCIAL_SECURITY_NUMBER_MIN_LENGTH)
        }
        RatingResource::ratingLevel required {
            enumIgnoreCase<RatingLevel>()
        }
        RatingResource::delayInMilliseconds required {}
        RatingResource::username required {
            minLength(length = USERNAME_MIN_LENGTH)
        }
        RatingResource::password required {
            minLength(length = PASSWORD_MIN_LENGTH)
        }
    }

    private inline fun <reified T : Enum<T>> ValidationBuilder<String>.enumIgnoreCase(): Constraint<String> {
        val enumNamesUpperCase = enumValues<T>().map { it.name.uppercase() }
        val enumNamesLowerCase = enumValues<T>().map { it.name.lowercase() }

        return addConstraint(
            errorMessage = "must be one of: {0}",
            templateValues = arrayOf(
                (enumNamesUpperCase + enumNamesLowerCase).joinToString(
                    separator = "', '",
                    prefix = "'",
                    postfix = "'",
                ),
            ),
        ) {
            it in enumNamesUpperCase + enumNamesLowerCase
        }
    }

    companion object {
        private const val SOCIAL_SECURITY_NUMBER_MIN_LENGTH = 1
        private const val USERNAME_MIN_LENGTH = 1
        private const val PASSWORD_MIN_LENGTH = 1
    }
}
