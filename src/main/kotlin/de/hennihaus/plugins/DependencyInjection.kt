package de.hennihaus.plugins

import de.hennihaus.configurations.configBackendModule
import de.hennihaus.configurations.defaultModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.fileProperties
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.koin.ktor.ext.getProperty as property

fun Application.configureDependencyInjection(vararg koinModules: Module) = install(plugin = Koin) {
    initKoin(modules = koinModules)
}

fun KoinApplication.initKoin(
    properties: Map<String, String> = emptyMap(),
    vararg modules: Module = arrayOf(defaultModule, configBackendModule),
) {
    slf4jLogger()
    fileProperties()
    properties(values = properties)
    modules(modules = modules)
}

fun <T> Application.getProperty(key: String): T = property(key) ?: throw PropertyNotFoundException(key = key)

fun <T> Routing.getProperty(key: String): T = property(key) ?: throw PropertyNotFoundException(key = key)

fun <T> Route.getProperty(key: String): T = property(key) ?: throw PropertyNotFoundException(key = key)

class PropertyNotFoundException(key: String) : IllegalStateException("${ErrorMessage.MISSING_PROPERTY_MESSAGE} $key")
