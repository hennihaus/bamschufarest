package de.hennihaus.utils

import io.konform.validation.Invalid
import io.konform.validation.Validation

fun <T> Validation<T>.validateAndThrowOnFailure(value: T) {
    val result = validate(value = value)
    if (result is Invalid<T>) {
        val messages = result.errors.map {
            "${it.dataPath.substringAfterLast(".")} ${it.message}"
        }
        throw ValidationException(message = "$messages")
    }
}

class ValidationException(override val message: String) : RuntimeException()
