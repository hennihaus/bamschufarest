package de.hennihaus.configurations

import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule as generatedModule

val defaultModule = module {
    includes(generatedModule)

    single {
        CIO.create()
    }
}

object Configuration {
    const val DEFAULT_CONFIG_FILE = "application.conf"

    const val BANK_UUID = "bank.uuid"

    const val ALLOWED_PROTOCOL = "ktor.cors.allowedProtocol"
    const val ALLOWED_HOST = "ktor.cors.allowedHost"
    const val TIMEZONE = "ktor.application.timezone"
    const val API_VERSION = "ktor.application.apiVersion"
}
