package de.hennihaus.objectmothers

import de.hennihaus.models.generated.Error
import de.hennihaus.plugins.ErrorMessage
import de.hennihaus.utils.withoutNanos
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object ErrorObjectMother {

    private const val DEFAULT_ZONE_ID = "Europe/Berlin"

    fun getInvalidRequestError(
        message: String = "[password must have at least 1 characters]",
        dateTime: Instant = Clock.System.now()
    ) = Error(
        message = message,
        dateTime = dateTime.toLocalDateTime(timeZone = TimeZone.of(zoneId = DEFAULT_ZONE_ID)).withoutNanos()
    )

    fun getNotFoundError(
        message: String = ErrorMessage.NOT_FOUND_MESSAGE,
        dateTime: Instant = Clock.System.now()
    ) = Error(
        message = message,
        dateTime = dateTime.toLocalDateTime(timeZone = TimeZone.of(zoneId = DEFAULT_ZONE_ID)).withoutNanos()
    )

    fun getInternalServerError(
        message: String = ErrorMessage.INTERNAL_SERVER_ERROR_MESSAGE,
        dateTime: Instant = Clock.System.now()
    ) = Error(
        message = message,
        dateTime = dateTime.toLocalDateTime(timeZone = TimeZone.of(zoneId = DEFAULT_ZONE_ID)).withoutNanos()
    )
}
