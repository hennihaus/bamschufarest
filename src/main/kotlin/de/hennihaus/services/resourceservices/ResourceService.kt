package de.hennihaus.services.resourceservices

import de.hennihaus.plugins.ValidationException
import io.konform.validation.Invalid
import io.konform.validation.Validation

interface ResourceService<Resource> {

    suspend fun resourceValidation(): Validation<Resource>

    suspend fun validate(resource: Resource): Resource {
        val result = resourceValidation().validate(value = resource)

        if (result is Invalid<Resource>) {
            val messages = result.errors.map {
                "${it.dataPath.substringAfterLast(".")} ${it.message}"
            }
            throw ValidationException(message = "$messages")
        }

        return resource
    }
}
