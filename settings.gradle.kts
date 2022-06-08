pluginManagement {
    val kotlinVersion: String by settings
    val ktLintVersion: String by settings
    val kotlinSymbolProcessingVersion: String by settings
    val koverVersion: String by settings
    val detektVersion: String by settings
    val shadowVersion: String by settings
    val openApiGeneratorVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
        id("org.jlleitschuh.gradle.ktlint") version ktLintVersion
        id("com.google.devtools.ksp") version kotlinSymbolProcessingVersion
        id("org.jetbrains.kotlinx.kover") version koverVersion
        id("io.gitlab.arturbosch.detekt") version detektVersion
        id("com.github.johnrengelman.shadow") version shadowVersion
        id("org.openapi.generator") version openApiGeneratorVersion
    }
}
