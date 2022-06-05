package de.hennihaus.services.callservices.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

object GroupPaths {
    const val GROUPS_PATH = "/groups"
    const val ID_PATH = "/{id}"
}

@Serializable
@Resource(GroupPaths.GROUPS_PATH)
class Groups {

    @Serializable
    @Resource(GroupPaths.ID_PATH)
    data class Id(val parent: Groups = Groups(), val id: String)
}
