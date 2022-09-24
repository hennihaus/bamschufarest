package de.hennihaus.objectmothers

import de.hennihaus.configurations.ConfigBackendConfiguration

object ConfigurationObjectMother {

    const val DEFAULT_PROTOCOL = "http"
    const val DEFAULT_HOST = "0.0.0.0"
    const val DEFAULT_PORT = 8080
    const val DEFAULT_API_VERSION = "v1"
    const val DEFAULT_MAX_RETRIES = 2

    fun getConfigBackendConfiguration(
        protocol: String = DEFAULT_PROTOCOL,
        host: String = DEFAULT_HOST,
        port: Int = DEFAULT_PORT,
        apiVersion: String = DEFAULT_API_VERSION,
        maxRetries: Int = DEFAULT_MAX_RETRIES,
    ) = ConfigBackendConfiguration(
        protocol = protocol,
        host = host,
        port = port,
        apiVersion = apiVersion,
        maxRetries = maxRetries,
    )
}
