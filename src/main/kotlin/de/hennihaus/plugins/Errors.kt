package de.hennihaus.plugins

import de.hennihaus.configurations.Configuration.TIMEZONE
import de.hennihaus.models.generated.Error
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
                zoneId = environment.config.property(path = TIMEZONE).getString(),
            ),
        )
        .withoutNanos()

    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is ValidationException -> call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = Error(
                        message = throwable.message,
                        dateTime = dateTime,
                    ),
                )
                is NotFoundException -> call.respond(
                    status = HttpStatusCode.NotFound,
                    message = Error(
                        message = "${throwable.message}",
                        dateTime = dateTime,
                    ),
                )
                else -> call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = Error(
                        message = "$throwable",
                        dateTime = dateTime,
                    ),
                )
            }
        }
    }
}

object ErrorMessage {
    const val MISSING_PROPERTY_MESSAGE = "Missing property"
}

class ValidationException(override val message: String) : RuntimeException()
