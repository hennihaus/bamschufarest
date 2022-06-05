package de.hennihaus.models

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val score: Int,
    val failureRiskInPercent: Double
)
