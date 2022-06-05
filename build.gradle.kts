import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import kotlinx.kover.api.CoverageEngine
import kotlinx.kover.api.VerificationValueType

plugins {
    application
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.kotlinx.kover")
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt")
    id("com.github.johnrengelman.shadow")
}

group = "de.hennihaus"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to "io.ktor.server.cio.EngineMain")
    }
}

sourceSets {
    main {
        java.srcDirs("build/generated/ksp/main/kotlin")
    }
}

repositories {
    mavenCentral()
}

configurations.all {
    // exclude kotlin test libraries
    exclude("org.jetbrains.kotlin", "kotlin-test")
    exclude("org.jetbrains.kotlin", "kotlin-test-common")
    exclude("org.jetbrains.kotlin", "kotlin-test-annotations-common")
    exclude("org.jetbrains.kotlin", "kotlin-test-junit")
}

dependencies {
    val ktorVersion: String by project
    val logbackVersion: String by project
    val kotestVersion: String by project
    val kotestLibariesVersion: String by project
    val mockkVersion: String by project
    val junitVersion: String by project
    val koinVersion: String by project
    val kotlinDateTimeVersion: String by project
    val koinAnnotationsVersion: String by project
    val konformVersion: String by project

    // ktor common plugins
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // ktor server plugins
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-resources-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cio-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")

    // ktor client plugins
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-resources-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktorVersion")

    // koin plugins
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    compileOnly("io.insert-koin:koin-annotations:$koinAnnotationsVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")
    ksp("io.insert-koin:koin-ksp-compiler:$koinAnnotationsVersion")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")

    // utility plugins
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:$kotlinDateTimeVersion")
    implementation("io.konform:konform-jvm:$konformVersion")

    // test plugins
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-ktor-jvm:$kotestLibariesVersion")
    testImplementation("io.kotest:kotest-assertions-kotlinx-time-jvm:$kotestLibariesVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

ktlint {
    ignoreFailures.set(false)
    filter {
        exclude("**/generated/**")
    }
}

detekt {
    config = files("config/detekt/detekt.yml")
    source = source.from(
        DetektExtension.DEFAULT_SRC_DIR_JAVA,
        DetektExtension.DEFAULT_TEST_SRC_DIR_JAVA,
        "src/integrationTest/java",
        DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
        DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
        "src/integrationTest/kotlin",
    )
}

tasks.init {
    dependsOn(tasks.ktlintApplyToIdea)
}

kover {
    coverageEngine.set(CoverageEngine.INTELLIJ)
}

tasks.koverMergedVerify {
    rule {
        bound {
            val minTestCoverageInPercent: String by project
            minValue = minTestCoverageInPercent.toInt()
            valueType = VerificationValueType.COVERED_LINES_PERCENTAGE
        }
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project)
                implementation(sourceSets.test.get().output)
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

tasks.check {
    dependsOn(testing.suites.named("integrationTest"))
}
