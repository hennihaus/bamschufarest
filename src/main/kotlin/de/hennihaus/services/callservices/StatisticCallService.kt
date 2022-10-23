package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Statistic
import de.hennihaus.configurations.ConfigBackendConfiguration
import de.hennihaus.configurations.Configuration.BANK_UUID
import de.hennihaus.services.callservices.paths.ConfigBackendPaths.INCREMENT_PATH
import de.hennihaus.services.callservices.paths.ConfigBackendPaths.STATISTICS_PATH
import de.hennihaus.utils.configureDefaultRequests
import de.hennihaus.utils.configureMonitoring
import de.hennihaus.utils.configureRetryBehavior
import de.hennihaus.utils.configureSerialization
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class StatisticCallService(
    @Property(BANK_UUID) private val defaultBankId: String,
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

    suspend fun incrementStatistic(bankId: UUID = UUID.fromString(defaultBankId), teamId: UUID): Statistic {
        val response = client.patch {
            url {
                appendPathSegments(
                    segments = listOf(
                        STATISTICS_PATH,
                        INCREMENT_PATH,
                    ),
                )
            }
            setBody(
                body = Statistic(
                    bankId = bankId,
                    teamId = teamId,
                    requestsCount = null,
                )
            )
        }
        return response.body()
    }
}
