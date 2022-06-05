package de.hennihaus.objectmothers

import de.hennihaus.routes.resources.Ratings

object ScoreObjectMother {

    fun getMinValidRequestScore(
        parent: Ratings = Ratings(),
        socialSecurityNumber: String? = "123",
        ratingLevel: String? = "A",
        delayInMilliseconds: Long? = 0L,
        username: String? = "H",
        password: String? = "H"
    ) = Ratings.Score(
        parent = parent,
        socialSecurityNumber = socialSecurityNumber,
        ratingLevel = ratingLevel,
        delayInMilliseconds = delayInMilliseconds,
        username = username,
        password = password
    )
}
