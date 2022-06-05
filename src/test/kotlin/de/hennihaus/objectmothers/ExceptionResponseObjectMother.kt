package de.hennihaus.objectmothers

import de.hennihaus.plugins.ErrorMessage
import de.hennihaus.plugins.ExceptionResponse
import de.hennihaus.utils.withoutNanos
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object ExceptionResponseObjectMother {

    private const val DEFAULT_ZONE_ID = "Europe/Berlin"

    fun getInvalidRequestExceptionResponse(
        message: String = "[password must have at least 1 characters]",
        dateTime: Instant = Clock.System.now()
    ) = ExceptionResponse(
        message = message,
        dateTime = dateTime.toLocalDateTime(timeZone = TimeZone.of(zoneId = DEFAULT_ZONE_ID)).withoutNanos()
    )

    fun getNotFoundExceptionResponse(
        message: String = ErrorMessage.NOT_FOUND_MESSAGE,
        dateTime: Instant = Clock.System.now()
    ) = ExceptionResponse(
        message = message,
        dateTime = dateTime.toLocalDateTime(timeZone = TimeZone.of(zoneId = DEFAULT_ZONE_ID)).withoutNanos()
    )

    fun getInternalServerExceptionResponse(
        message: String = ErrorMessage.INTERNAL_SERVER_ERROR_MESSAGE,
        dateTime: Instant = Clock.System.now()
    ) = ExceptionResponse(
        message = message,
        dateTime = dateTime.toLocalDateTime(timeZone = TimeZone.of(zoneId = DEFAULT_ZONE_ID)).withoutNanos()
    )
}
