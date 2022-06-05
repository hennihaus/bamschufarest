package de.hennihaus.utils

import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TimeUtilsTest {

    @Nested
    inner class WithoutNanos {
        @Test
        fun `should convert local date time to local date time without nanoseconds`() {
            val dateTimeUnderTest = LocalDateTime(
                year = DEFAULT_DATE_TIME_YEAR,
                month = Month.APRIL,
                dayOfMonth = DEFAULT_DATE_TIME_VALUE,
                hour = DEFAULT_DATE_TIME_VALUE,
                minute = DEFAULT_DATE_TIME_VALUE,
                second = DEFAULT_DATE_TIME_VALUE,
                nanosecond = DEFAULT_DATE_TIME_VALUE,
            )

            val result: LocalDateTime = dateTimeUnderTest.withoutNanos()

            result shouldBe LocalDateTime(
                year = DEFAULT_DATE_TIME_YEAR,
                month = Month.APRIL,
                dayOfMonth = DEFAULT_DATE_TIME_VALUE,
                hour = DEFAULT_DATE_TIME_VALUE,
                minute = DEFAULT_DATE_TIME_VALUE,
                second = DEFAULT_DATE_TIME_VALUE,
                nanosecond = 0,
            )
        }
    }

    companion object {
        private const val DEFAULT_DATE_TIME_YEAR = 2015
        private const val DEFAULT_DATE_TIME_VALUE = 15
    }
}
