package de.hennihaus.models

enum class RatingLevel(
    val minScore: Int,
    val maxScore: Int,
    val failureRiskInPercent: Double
) {
    A(minScore = 9858, maxScore = 9999, failureRiskInPercent = 0.77),
    B(minScore = 9752, maxScore = 9857, failureRiskInPercent = 1.79),
    C(minScore = 9678, maxScore = 9751, failureRiskInPercent = 2.88),
    D(minScore = 9580, maxScore = 9677, failureRiskInPercent = 3.82),
    E(minScore = 9435, maxScore = 9579, failureRiskInPercent = 5.06),
    F(minScore = 9196, maxScore = 9434, failureRiskInPercent = 7.06),
    G(minScore = 8712, maxScore = 9195, failureRiskInPercent = 10.25),
    H(minScore = 8140, maxScore = 8711, failureRiskInPercent = 16.01),
    I(minScore = 7657, maxScore = 8139, failureRiskInPercent = 21.14),
    J(minScore = 7247, maxScore = 7656, failureRiskInPercent = 24.69),
    K(minScore = 6545, maxScore = 7246, failureRiskInPercent = 28.70),
    L(minScore = 4612, maxScore = 6544, failureRiskInPercent = 39.86),
    N(minScore = 3799, maxScore = 4611, failureRiskInPercent = 52.34),
    O(minScore = 786, maxScore = 3798, failureRiskInPercent = 79.38),
    P(minScore = 1, maxScore = 785, failureRiskInPercent = 98.07)
}
