package de.hennihaus.objectmothers

import de.hennihaus.models.generated.Errors
import de.hennihaus.models.generated.Reason
import de.hennihaus.objectmothers.ReasonObjectMother.getInternalServerErrorReason
import de.hennihaus.objectmothers.ReasonObjectMother.getInvalidPasswordReason
import de.hennihaus.objectmothers.ReasonObjectMother.getMissingParameterReason
import de.hennihaus.objectmothers.ReasonObjectMother.getTeamNotFoundReason
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

object ErrorsObjectMother {

    fun getInvalidRequestErrors(
        reasons: List<Reason> = getInvalidRequestReasons(),
        dateTime: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
    ) = Errors(
        reasons = reasons,
        dateTime = dateTime.truncatedTo(ChronoUnit.SECONDS),
    )

    fun getMissingParameterErrors(
        reasons: List<Reason> = getMissingParameterReasons(),
        dateTime: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
    ) = Errors(
        reasons = reasons,
        dateTime = dateTime.truncatedTo(ChronoUnit.SECONDS),
    )

    fun getNotFoundErrors(
        reasons: List<Reason> = getNotFoundReasons(),
        dateTime: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
    ) = Errors(
        reasons = reasons,
        dateTime = dateTime.truncatedTo(ChronoUnit.SECONDS),
    )

    fun getInternalServerErrors(
        reasons: List<Reason> = getInternalServerErrorReasons(),
        dateTime: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
    ) = Errors(
        reasons = reasons,
        dateTime = dateTime.truncatedTo(ChronoUnit.SECONDS),
    )

    private fun getInvalidRequestReasons() = listOf(
        getInvalidPasswordReason(),
    )

    private fun getMissingParameterReasons() = listOf(
        getMissingParameterReason(),
    )

    private fun getNotFoundReasons() = listOf(
        getTeamNotFoundReason(),
    )

    private fun getInternalServerErrorReasons() = listOf(
        getInternalServerErrorReason(),
    )
}
