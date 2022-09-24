package de.hennihaus.services.callservices.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

object BankPaths {
    const val BANKS_PATH = "/banks"
    const val ID_PATH = "/{id}"
}

@Serializable
@Resource(BankPaths.BANKS_PATH)
class Banks {

    @Serializable
    @Resource(BankPaths.ID_PATH)
    data class Id(val parent: Banks = Banks(), val id: String)
}
