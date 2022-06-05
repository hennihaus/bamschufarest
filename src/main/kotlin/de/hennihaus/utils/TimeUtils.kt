package de.hennihaus.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime

fun LocalDateTime.withoutNanos(): LocalDateTime = date.atTime(
    hour = hour,
    minute = minute,
    second = second,
)
