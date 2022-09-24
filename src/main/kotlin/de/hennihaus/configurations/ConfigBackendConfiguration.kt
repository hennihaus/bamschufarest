package de.hennihaus.configurations

import de.hennihaus.configurations.ConfigBackendConfiguration.Companion.CONFIG_BACKEND_API_VERSION
import de.hennihaus.configurations.ConfigBackendConfiguration.Companion.CONFIG_BACKEND_HOST
import de.hennihaus.configurations.ConfigBackendConfiguration.Companion.CONFIG_BACKEND_PORT
import de.hennihaus.configurations.ConfigBackendConfiguration.Companion.CONFIG_BACKEND_PROTOCOL
import de.hennihaus.configurations.ConfigBackendConfiguration.Companion.CONFIG_BACKEND_RETRIES
import org.koin.dsl.module

val configBackendModule = module {
    single {
        val protocol = getProperty<String>(key = CONFIG_BACKEND_PROTOCOL)
        val host = getProperty<String>(key = CONFIG_BACKEND_HOST)
        val port = getProperty<String>(key = CONFIG_BACKEND_PORT)
        val apiVersion = getProperty<String>(key = CONFIG_BACKEND_API_VERSION)
        val maxRetries = getProperty<String>(key = CONFIG_BACKEND_RETRIES)

        ConfigBackendConfiguration(
            protocol = protocol,
            host = host,
            port = port.toInt(),
            apiVersion = apiVersion,
            maxRetries = maxRetries.toInt(),
        )
    }
}

data class ConfigBackendConfiguration(
    val protocol: String,
    val host: String,
    val port: Int,
    val apiVersion: String,
    val maxRetries: Int,
) {
    companion object {
        const val CONFIG_BACKEND_PROTOCOL = "configBackend.protocol"
        const val CONFIG_BACKEND_HOST = "configBackend.host"
        const val CONFIG_BACKEND_PORT = "configBackend.port"
        const val CONFIG_BACKEND_API_VERSION = "configBackend.apiVersion"
        const val CONFIG_BACKEND_RETRIES = "configBackend.retries"
    }
}
