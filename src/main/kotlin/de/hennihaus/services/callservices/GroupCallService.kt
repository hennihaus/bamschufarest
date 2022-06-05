package de.hennihaus.services.callservices

import de.hennihaus.models.Group

interface GroupCallService {
    suspend fun getAllGroups(): List<Group>

    suspend fun updateGroup(id: String, group: Group): Group
}
