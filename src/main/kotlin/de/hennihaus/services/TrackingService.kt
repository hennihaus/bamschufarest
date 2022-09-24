package de.hennihaus.services

import de.hennihaus.services.callservices.BankCallService
import de.hennihaus.services.callservices.StatisticCallService
import io.ktor.server.plugins.NotFoundException
import org.koin.core.annotation.Single

@Single
class TrackingService(
    private val bankCall: BankCallService,
    private val statisticCall: StatisticCallService,
) {

    suspend fun trackRequest(username: String, password: String) {
        val team = bankCall.getBankById().teams.find {
            (it.username == username) and (it.password == password)
        }
        with(team ?: throw NotFoundException(message = TEAM_NOT_FOUND_MESSAGE)) {
            statisticCall.incrementStatistic(
                teamId = uuid,
            )
        }
    }

    companion object {
        const val TEAM_NOT_FOUND_MESSAGE = "[team not found by username and password]"
    }
}
