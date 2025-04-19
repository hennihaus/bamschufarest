package de.hennihaus.plugins

import de.hennihaus.models.generated.Errors
import de.hennihaus.models.generated.Reason
import de.hennihaus.plugins.ErrorMessage.ANONYMOUS_OBJECT
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.MissingRequestParameterException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

fun Application.configureErrorHandling() = install(plugin = StatusPages) {
    exception<Throwable> { call, throwable ->
        val dateTime = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)

        when (throwable) {
            is RequestValidationException -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = throwable.toErrors(dateTime = dateTime),
            )
            is MissingRequestParameterException -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = throwable.toErrors(dateTime = dateTime),
            )
            is NotFoundException -> call.respond(
                status = HttpStatusCode.NotFound,
                message = throwable.toErrors(dateTime = dateTime),
            )
            else -> call.also { it.application.environment.log.error("Internal Server Error: ", throwable) }
                .respond(
                    status = HttpStatusCode.InternalServerError,
                    message = throwable.toErrors(dateTime = dateTime),
                )
        }
    }
}

private fun Throwable.toErrors(dateTime: OffsetDateTime) = Errors(
    reasons = listOf(
        Reason(
            exception = this::class.simpleName ?: ANONYMOUS_OBJECT,
            message = "$message".replaceFirstChar { it.lowercase() },
        ),
    ),
    dateTime = dateTime,
)

private fun RequestValidationException.toErrors(dateTime: OffsetDateTime) = Errors(
    reasons = this.reasons.map { reason ->
        Reason(
            exception = this::class.simpleName ?: ANONYMOUS_OBJECT,
            message = reason.replaceFirstChar { it.lowercase() },
        )
    },
    dateTime = dateTime,
)

object ErrorMessage {
    const val ANONYMOUS_OBJECT = "Anonymous Object"

    const val MISSING_PROPERTY_MESSAGE = "missing property"
}

class RequestValidationException(val reasons: List<String>) : IllegalArgumentException(
    "Validation failed. Reasons: ${reasons.joinToString(", ")}",
)
