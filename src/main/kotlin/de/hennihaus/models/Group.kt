package de.hennihaus.models

import de.hennihaus.configurations.ConfigBackendConfiguration.Companion.GROUP_ID_FIELD
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    @SerialName(GROUP_ID_FIELD)
    val id: String,
    val username: String,
    val password: String,
    val jmsQueue: String,
    val students: List<String>,
    val stats: Map<String, Int>,
    val hasPassed: Boolean,
)
