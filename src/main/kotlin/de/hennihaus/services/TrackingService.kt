package de.hennihaus.services

import de.hennihaus.bamdatamodel.TeamType
import de.hennihaus.services.callservices.StatisticCallService
import de.hennihaus.services.callservices.TeamCallService
import io.ktor.server.plugins.NotFoundException
import org.koin.core.annotation.Single

@Single
class TrackingService(
    private val teamCall: TeamCallService,
    private val statisticCall: StatisticCallService,
) {

    suspend fun trackRequest(username: String, password: String, origin: String?) {
        val team = teamCall.getTeams(username = username, password = password).find {
            (it.username == username) and (it.password == password)
        }
        with(team ?: throw NotFoundException(message = TEAM_NOT_FOUND_MESSAGE)) {
            takeIf { (type == TeamType.REGULAR) and (origin !is String) }?.run {
                statisticCall.incrementStatistic(
                    teamId = uuid,
                )
            }
            takeIf { (type == TeamType.EXAMPLE) and (origin is String) }?.run {
                statisticCall.incrementStatistic(
                    teamId = uuid,
                )
            }
        }
    }

    companion object {
        const val TEAM_NOT_FOUND_MESSAGE = "team not found by username and password"
    }
}
