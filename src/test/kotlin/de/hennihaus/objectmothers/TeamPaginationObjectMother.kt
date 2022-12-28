package de.hennihaus.objectmothers

import de.hennihaus.bamdatamodel.Team
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.getFirstTeam
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.getSecondTeam
import de.hennihaus.bamdatamodel.objectmothers.TeamObjectMother.getThirdTeam
import de.hennihaus.models.TeamPagination

object TeamPaginationObjectMother {

    fun getDefaultTeamPagination(
        items: List<Team> = getTeamItems(),
    ) = TeamPagination(
        items = items,
    )

    private fun getTeamItems() = listOf(
        getFirstTeam(),
        getSecondTeam(),
        getThirdTeam(),
    )
}
