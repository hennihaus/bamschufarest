package de.hennihaus.testutils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun bamObjectMapper(): ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule()).disable(
    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
)
