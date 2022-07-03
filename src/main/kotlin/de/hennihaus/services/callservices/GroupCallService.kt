package de.hennihaus.services.callservices

import de.hennihaus.configurations.ConfigBackendConfiguration
import de.hennihaus.models.Group
import de.hennihaus.services.callservices.resources.Groups
import de.hennihaus.utils.configureMonitoring
import de.hennihaus.utils.configureRetryBehavior
import de.hennihaus.utils.configureSerialization
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.util.appendIfNameAbsent
import org.koin.core.annotation.Single

@Single
class GroupCallService(
    private val engine: HttpClientEngine,
    private val config: ConfigBackendConfiguration,
) {

    private val client = HttpClient(engine = engine) {
        expectSuccess = true
        configureMonitoring()
        configureSerialization()
        configureRetryBehavior(maxRetries = config.maxRetries)
        configureDefaultRequests()
    }

    suspend fun getAllGroups(): List<Group> = client.get(resource = Groups()).body()

    suspend fun updateGroup(id: String, group: Group): Group {
        val response = client.put(resource = Groups.Id(id = id)) {
            setBody(body = group)
        }
        return response.body()
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
