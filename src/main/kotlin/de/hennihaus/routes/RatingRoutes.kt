package de.hennihaus.routes

import de.hennihaus.routes.resources.RatingResource
import de.hennihaus.routes.resources.validate
import de.hennihaus.services.RatingService
import de.hennihaus.services.TrackingService
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import org.koin.java.KoinJavaComponent.getKoin

fun Route.registerRatingRoutes() {
    getRating()
}

private fun Route.getRating() = get<RatingResource> { request ->
    request.validate {
        val rating = getKoin().get<RatingService>()
        val tracking = getKoin().get<TrackingService>()

        val score = rating.calculateRating(
            ratingLevel = it.ratingLevel,
            delayInMilliseconds = it.delayInMilliseconds,
        )
        call.respond(
            message = score.also { _ ->
                tracking.trackRequest(
                    username = it.username ?: "",
                    password = it.password ?: "",
                )
            },
        )
    }
}
