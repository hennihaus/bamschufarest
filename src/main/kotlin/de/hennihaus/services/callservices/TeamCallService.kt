package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Team
import de.hennihaus.configurations.ConfigBackendConfiguration
import de.hennihaus.models.TeamPagination
import de.hennihaus.services.callservices.paths.ConfigBackendPaths.PASSWORD_QUERY_PARAMETER
import de.hennihaus.services.callservices.paths.ConfigBackendPaths.TEAMS_PATH
import de.hennihaus.services.callservices.paths.ConfigBackendPaths.USERNAME_QUERY_PARAMETER
import de.hennihaus.utils.configureDefaultRequests
import de.hennihaus.utils.configureMonitoring
import de.hennihaus.utils.configureRetryBehavior
import de.hennihaus.utils.configureSerialization
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import org.koin.core.annotation.Single

@Single
class TeamCallService(
    private val engine: HttpClientEngine,
    private val config: ConfigBackendConfiguration,
) {

    private val client = HttpClient(engine = engine) {
        expectSuccess = true
        configureMonitoring()
        configureSerialization()
        configureRetryBehavior(
            maxRetries = config.maxRetries,
        )
        configureDefaultRequests(
            protocol = config.protocol,
            host = config.host,
            port = config.port,
            apiVersion = config.apiVersion,
        )
    }

    suspend fun getTeams(username: String, password: String): List<Team> {
        val response = client.get {
            url {
                appendPathSegments(segments = listOf(TEAMS_PATH))

                parameters.append(name = USERNAME_QUERY_PARAMETER, value = username)
                parameters.append(name = PASSWORD_QUERY_PARAMETER, value = password)
            }
        }
        return response.body<TeamPagination>().items
    }
}
