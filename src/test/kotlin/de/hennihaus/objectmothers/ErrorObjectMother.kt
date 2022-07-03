package de.hennihaus.objectmothers

import de.hennihaus.models.generated.Error
import de.hennihaus.utils.withoutNanos
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object ErrorObjectMother {

    const val DEFAULT_ZONE_ID = "Europe/Berlin"
    const val DEFAULT_INVALID_REQUEST_ERROR_MESSAGE = "[password must have at least 1 characters]"
    const val DEFAULT_NOT_FOUND_ERROR_MESSAGE = "[resource not found]"

    fun getInvalidRequestError(
        message: String = DEFAULT_INVALID_REQUEST_ERROR_MESSAGE,
        dateTime: Instant = Clock.System.now(),
    ) = Error(
        message = message,
        dateTime = dateTime.toLocalDateTime(timeZone = TimeZone.of(zoneId = DEFAULT_ZONE_ID)).withoutNanos(),
    )

    fun getNotFoundError(
        message: String = DEFAULT_NOT_FOUND_ERROR_MESSAGE,
        dateTime: Instant = Clock.System.now(),
    ) = Error(
        message = message,
        dateTime = dateTime.toLocalDateTime(timeZone = TimeZone.of(zoneId = DEFAULT_ZONE_ID)).withoutNanos(),
    )

    fun getInternalServerError(
        message: String = "${IllegalStateException()}",
        dateTime: Instant = Clock.System.now(),
    ) = Error(
        message = message,
        dateTime = dateTime.toLocalDateTime(timeZone = TimeZone.of(zoneId = DEFAULT_ZONE_ID)).withoutNanos(),
    )
}
