package de.hennihaus

import de.hennihaus.configurations.Configuration.DEFAULT_CONFIG_FILE
import de.hennihaus.configurations.configBackendModule
import de.hennihaus.configurations.defaultModule
import de.hennihaus.plugins.configureCors
import de.hennihaus.plugins.configureDependencyInjection
import de.hennihaus.plugins.configureErrorHandling
import de.hennihaus.plugins.configureMonitoring
import de.hennihaus.plugins.configureRouting
import de.hennihaus.plugins.configureValidation
import io.ktor.server.application.Application
import io.ktor.server.cio.EngineMain
import org.koin.core.module.Module

fun main(args: Array<String>) = EngineMain.main(args = args)

fun Application.module(
    configFilePath: String = DEFAULT_CONFIG_FILE,
    vararg koinModules: Module = arrayOf(defaultModule, configBackendModule),
) {
    configureMonitoring()
    configureDependencyInjection(configFilePath = configFilePath, koinModules = koinModules)
    configureCors()
    configureRouting()
    configureValidation()
    configureErrorHandling()
}
