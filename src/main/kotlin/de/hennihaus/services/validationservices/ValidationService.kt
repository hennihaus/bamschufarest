package de.hennihaus.services.validationservices

import de.hennihaus.plugins.RequestValidationException
import io.konform.validation.Invalid
import io.konform.validation.Validation

interface ValidationService<Resource> {

    suspend fun urlValidation(resource: Resource): Validation<Resource>

    suspend fun validateUrl(resource: Resource) {
        val result = urlValidation(resource = resource).validate(value = resource)

        if (result is Invalid<Resource>) {
            throw RequestValidationException(
                reasons = result.errors.map {
                    "${it.dataPath.substringAfter(delimiter = ".")} ${it.message}"
                }
            )
        }
    }
}
