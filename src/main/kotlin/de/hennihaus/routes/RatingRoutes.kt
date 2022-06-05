package de.hennihaus.routes

import de.hennihaus.routes.resources.Ratings
import de.hennihaus.routes.resources.validate
import de.hennihaus.services.RatingService
import de.hennihaus.services.TrackingService
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import org.koin.java.KoinJavaComponent.getKoin

fun Route.registerRatingRoutes() {
    getRatingScore()
}

private fun Route.getRatingScore() = get<Ratings.Score> { request ->
    request.validate {
        val ratingService = getKoin().get<RatingService>()
        val trackingService = getKoin().get<TrackingService>()

        val score = ratingService.calculateScore(
            ratingLevel = it.ratingLevel,
            delayInMilliseconds = it.delayInMilliseconds,
        )
        call.respond(
            message = score.also { _ ->
                trackingService.trackRequest(
                    username = it.username,
                    password = it.password,
                )
            },
        )
    }
}
