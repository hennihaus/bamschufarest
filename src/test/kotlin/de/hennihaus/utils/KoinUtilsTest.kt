package de.hennihaus.utils

import io.kotest.matchers.maps.shouldContainAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KoinUtilsTest {

    @Nested
    inner class GetHoconFileAsProperties {
        @Test
        fun `should return hocon config file as properties`() {
            val file = "koin-test.conf"

            val result: Map<String, String> = getHoconFileAsProperties(
                file = file
            )

            result shouldContainAll mapOf(
                "configOnFirstLevel" to "8080",
                "configOnSecondLevel.string" to "test",
                "configOnSecondLevel.stringList[0]" to "test",
                "configOnSecondLevel.stringList[1]" to "8080",
            )
        }
    }
}
