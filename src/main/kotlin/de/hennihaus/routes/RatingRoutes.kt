package de.hennihaus.routes

import de.hennihaus.routes.resources.RatingResource
import de.hennihaus.services.RatingService
import de.hennihaus.services.TrackingService
import de.hennihaus.services.resourceservices.RatingResourceService
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import org.koin.java.KoinJavaComponent.getKoin

fun Route.registerRatingRoutes() {
    getRating()
}

private fun Route.getRating() = get<RatingResource> { request ->
    val ratingResource = getKoin().get<RatingResourceService>()
    val rating = getKoin().get<RatingService>()
    val tracking = getKoin().get<TrackingService>()

    ratingResource.validate(resource = request) {
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
