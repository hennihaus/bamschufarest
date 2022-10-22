package de.hennihaus.services.validationservices.resources

data class RatingResource(
    val socialSecurityNumber: String?,
    val ratingLevel: String?,
    val delayInMilliseconds: String?,
    val username: String?,
    val password: String?,
)
