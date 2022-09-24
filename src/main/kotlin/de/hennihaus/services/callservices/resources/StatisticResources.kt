package de.hennihaus.services.callservices.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

object StatisticPaths {
    const val STATISTICS_PATH = "/statistics"

    const val INCREMENT_PATH = "/increment"
}

@Serializable
@Resource(StatisticPaths.STATISTICS_PATH)
class Statistics {

    @Serializable
    @Resource(StatisticPaths.INCREMENT_PATH)
    data class Increment(val parent: Statistics = Statistics())
}
