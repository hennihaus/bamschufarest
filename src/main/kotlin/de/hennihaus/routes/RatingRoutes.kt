package de.hennihaus.routes

import de.hennihaus.routes.RatingRoutes.BAM_ORIGIN_HEADER
import de.hennihaus.routes.RatingRoutes.DELAY_IN_MILLISECONDS_QUERY_PARAMETER
import de.hennihaus.routes.RatingRoutes.PASSWORD_QUERY_PARAMETER
import de.hennihaus.routes.RatingRoutes.RATING_LEVEL_QUERY_PARAMETER
import de.hennihaus.routes.RatingRoutes.RATING_PATH
import de.hennihaus.routes.RatingRoutes.USERNAME_QUERY_PARAMETER
import de.hennihaus.services.RatingService
import de.hennihaus.services.TrackingService
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import org.koin.java.KoinJavaComponent.getKoin

object RatingRoutes {
    const val RATING_PATH = "rating"

    const val SOCIAL_SECURITY_NUMBER_QUERY_PARAMETER = "socialSecurityNumber"
    const val RATING_LEVEL_QUERY_PARAMETER = "ratingLevel"
    const val DELAY_IN_MILLISECONDS_QUERY_PARAMETER = "delayInMilliseconds"
    const val USERNAME_QUERY_PARAMETER = "username"
    const val PASSWORD_QUERY_PARAMETER = "password"

    const val BAM_ORIGIN_HEADER = "X-BAM-Origin"
}

fun Route.registerRatingRoutes() {
    getRating()
}

private fun Route.getRating() = get(path = "/$RATING_PATH") {
    val rating = getKoin().get<RatingService>()
    val tracking = getKoin().get<TrackingService>()

    with(receiver = call) {
        val ratingLevel = request.queryParameters.getOrFail(name = RATING_LEVEL_QUERY_PARAMETER)
        val delayInMilliseconds = request.queryParameters.getOrFail(name = DELAY_IN_MILLISECONDS_QUERY_PARAMETER)
        val username = request.queryParameters.getOrFail(name = USERNAME_QUERY_PARAMETER)
        val password = request.queryParameters.getOrFail(name = PASSWORD_QUERY_PARAMETER)
        val origin = request.headers.get(name = BAM_ORIGIN_HEADER)

        val score = rating.calculateRating(
            ratingLevel = ratingLevel,
            delayInMilliseconds = delayInMilliseconds.toLongOrNull(),
        )

        call.respond(
            message = score.also {
                tracking.trackRequest(
                    username = username,
                    password = password,
                    origin = origin,
                )
            },
        )
    }
}
