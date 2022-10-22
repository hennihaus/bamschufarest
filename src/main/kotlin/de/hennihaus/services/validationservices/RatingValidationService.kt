package de.hennihaus.services.validationservices

import de.hennihaus.models.RatingLevel
import de.hennihaus.services.validationservices.resources.RatingResource
import io.konform.validation.Constraint
import io.konform.validation.Validation
import io.konform.validation.ValidationBuilder
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import org.koin.core.annotation.Single

@Single
class RatingValidationService : ValidationService<RatingResource> {

    override suspend fun urlValidation(resource: RatingResource): Validation<RatingResource> = Validation {
        RatingResource::socialSecurityNumber required {
            minLength(length = SOCIAL_SECURITY_NUMBER_MIN_LENGTH)
        }
        RatingResource::ratingLevel required {
            enumIgnoreCase<RatingLevel>()
        }
        RatingResource::delayInMilliseconds required {
            longType()
        }
        RatingResource::username required {
            minLength(length = USERNAME_MIN_LENGTH)
            maxLength(length = USERNAME_MAX_LENGTH)
        }
        RatingResource::password required {
            minLength(length = PASSWORD_MIN_LENGTH)
            maxLength(length = PASSWORD_MAX_LENGTH)
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

    private fun ValidationBuilder<String>.longType() = addConstraint(
        errorMessage = "must be a number",
    ) {
        it.toLongOrNull() is Long
    }

    companion object {
        const val SOCIAL_SECURITY_NUMBER_MIN_LENGTH = 1
        const val USERNAME_MIN_LENGTH = 6
        const val USERNAME_MAX_LENGTH = 50
        const val PASSWORD_MIN_LENGTH = 8
        const val PASSWORD_MAX_LENGTH = 50
    }
}
