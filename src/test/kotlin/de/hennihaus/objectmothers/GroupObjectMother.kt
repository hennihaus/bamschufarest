package de.hennihaus.objectmothers

import de.hennihaus.models.Group

object GroupObjectMother {

    const val SCHUFA_BANK_NAME = "schufa"
    const val V_BANK_NAME = "vbank"
    const val JMS_BANK_A_NAME = "jmsBankA"

    private const val DEFAULT_PASSWORD = "0123456789"
    private const val DEFAULT_HAS_PASSED = false
    private val DEFAULT_STUDENTS = listOf(
        "Angelar Merkel",
        "Max Mustermann",
        "Thomas Müller"
    )
    val ZERO_STATS = mapOf(
        SCHUFA_BANK_NAME to 0,
        V_BANK_NAME to 0,
        JMS_BANK_A_NAME to 0
    )

    fun getFirstGroup(
        id: String = "61320a79410347e41dbea0f9",
        username: String = "LoanBrokerGruppe01",
        password: String = DEFAULT_PASSWORD,
        jmsQueue: String = "ResponseLoanBrokerGruppe01",
        students: List<String> = DEFAULT_STUDENTS,
        stats: Map<String, Int> = ZERO_STATS,
        hasPassed: Boolean = DEFAULT_HAS_PASSED
    ) = Group(
        id = id,
        username = username,
        password = password,
        jmsQueue = jmsQueue,
        students = students,
        stats = stats,
        hasPassed = hasPassed
    )

    fun getSecondGroup(
        id: String = "61320a84befcde533be505c5",
        username: String = "LoanBrokerGruppe02",
        password: String = DEFAULT_PASSWORD,
        jmsQueue: String = "ResponseLoanBrokerGruppe02",
        students: List<String> = DEFAULT_STUDENTS,
        stats: Map<String, Int> = ZERO_STATS,
        hasPassed: Boolean = DEFAULT_HAS_PASSED
    ) = Group(
        id = id,
        username = username,
        password = password,
        jmsQueue = jmsQueue,
        students = students,
        stats = stats,
        hasPassed = hasPassed
    )

    fun getThirdGroup(
        id: String = "62449e3d944f2af727e6f1fb",
        username: String = "LoanBrokerGruppe03",
        password: String = DEFAULT_PASSWORD,
        jmsQueue: String = "ResponseLoanBrokerGruppe03",
        students: List<String> = DEFAULT_STUDENTS,
        stats: Map<String, Int> = ZERO_STATS,
        hasPassed: Boolean = DEFAULT_HAS_PASSED
    ) = Group(
        id = id,
        username = username,
        password = password,
        jmsQueue = jmsQueue,
        students = students,
        stats = stats,
        hasPassed = hasPassed
    )
}
