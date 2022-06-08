package de.hennihaus.plugins

import de.hennihaus.models.generated.Error
import de.hennihaus.plugins.ErrorMessage.INTERNAL_SERVER_ERROR_MESSAGE
import de.hennihaus.plugins.ErrorMessage.NOT_FOUND_MESSAGE
import de.hennihaus.utils.ValidationException
import de.hennihaus.utils.withoutNanos
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Application.configureErrorHandling() {
    val dateTime = Clock.System.now()
        .toLocalDateTime(
            timeZone = TimeZone.of(
                zoneId = getProperty(key = "ktor.application.timezoneId")
            )
        )
        .withoutNanos()

    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is ValidationException -> call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = Error(
                        message = throwable.message,
                        dateTime = dateTime
                    )
                )
                is NotFoundException -> call.respond(
                    status = HttpStatusCode.NotFound,
                    message = Error(
                        message = throwable.message ?: NOT_FOUND_MESSAGE,
                        dateTime = dateTime
                    )
                )
                else -> call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = Error(
                        message = throwable.message ?: INTERNAL_SERVER_ERROR_MESSAGE,
                        dateTime = dateTime
                    )
                )
            }
        }
    }
}

object ErrorMessage {
    const val NOT_FOUND_MESSAGE = "[resource not found]"
    const val INTERNAL_SERVER_ERROR_MESSAGE = "[internal server error]"
    const val MISSING_PROPERTY_MESSAGE = "Missing property"
}
