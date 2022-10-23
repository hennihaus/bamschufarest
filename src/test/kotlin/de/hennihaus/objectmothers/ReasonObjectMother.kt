package de.hennihaus.objectmothers

import de.hennihaus.models.generated.Reason
import de.hennihaus.plugins.RequestValidationException
import de.hennihaus.services.TrackingService.Companion.TEAM_NOT_FOUND_MESSAGE
import io.ktor.server.plugins.MissingRequestParameterException
import io.ktor.server.plugins.NotFoundException

object ReasonObjectMother {

    const val DEFAULT_INVALID_REQUEST_MESSAGE = "password is required"
    const val DEFAULT_MISSING_PARAMETER_MESSAGE = "request parameter password is missing"

    fun getInvalidPasswordReason(
        exception: String = RequestValidationException::class.simpleName!!,
        message: String = DEFAULT_INVALID_REQUEST_MESSAGE,
    ) = Reason(
        exception = exception,
        message = message,
    )

    fun getMissingParameterReason(
        exception: String = MissingRequestParameterException::class.simpleName!!,
        message: String = DEFAULT_MISSING_PARAMETER_MESSAGE,
    ) = Reason(
        exception = exception,
        message = message,
    )

    fun getTeamNotFoundReason(
        exception: String = NotFoundException::class.simpleName!!,
        message: String = TEAM_NOT_FOUND_MESSAGE,
    ) = Reason(
        exception = exception,
        message = message,
    )

    fun getInternalServerErrorReason(
        exception: String = IllegalStateException::class.simpleName!!,
        message: String = "${IllegalStateException().message}",
    ) = Reason(
        exception = exception,
        message = message,
    )
}
