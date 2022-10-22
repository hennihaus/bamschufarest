package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Bank
import de.hennihaus.configurations.ConfigBackendConfiguration
import de.hennihaus.configurations.Configuration.BANK_UUID
import de.hennihaus.services.callservices.paths.ConfigBackendPaths.BANKS_PATH
import de.hennihaus.utils.configureDefaultRequests
import de.hennihaus.utils.configureMonitoring
import de.hennihaus.utils.configureRetryBehavior
import de.hennihaus.utils.configureSerialization
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class BankCallService(
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

    suspend fun getBankById(id: UUID = UUID.fromString(defaultBankId)): Bank {
        val response = client.get {
            url {
                appendPathSegments(
                    segments = listOf(
                        BANKS_PATH,
                        "$id"
                    ),
                )
            }
        }
        return response.body()
    }
}
