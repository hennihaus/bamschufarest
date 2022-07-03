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
