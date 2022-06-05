package de.hennihaus.services.callservices

import de.hennihaus.configurations.ConfigBackendConfiguration
import de.hennihaus.models.Group
import de.hennihaus.services.callservices.resources.Groups
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class GroupCallServiceImpl(
    private val engine: HttpClientEngine,
    private val config: ConfigBackendConfiguration
) : GroupCallService {

    private val client = HttpClient(engine = engine) {
        expectSuccess = true
        configureMonitoring()
        configureSerialization()
        configureRetryBehavior()
        configureDefaultRequests()
    }

    override suspend fun getAllGroups(): List<Group> = client.get(resource = Groups()).body()

    override suspend fun updateGroup(id: String, group: Group): Group {
        val response = client.put(resource = Groups.Id(id = id)) {
            setBody(body = group)
        }
        return response.body()
    }

    private fun HttpClientConfig<*>.configureMonitoring() = install(plugin = Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.INFO
    }

    private fun HttpClientConfig<*>.configureSerialization() {
        install(plugin = ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                },
            )
        }
        install(plugin = Resources)
    }

    private fun HttpClientConfig<*>.configureRetryBehavior() = install(plugin = HttpRequestRetry) {
        retryOnServerErrors(maxRetries = config.maxRetries)
        exponentialDelay()
    }

    private fun HttpClientConfig<*>.configureDefaultRequests() = install(plugin = DefaultRequest) {
        val (protocol, host, port) = config
        url {
            this.protocol = URLProtocol.createOrDefault(name = protocol)
            this.host = host
            this.port = port
        }
        headers {
            appendIfNameAbsent(name = HttpHeaders.ContentType, value = "${ContentType.Application.Json}")
        }
    }
}
