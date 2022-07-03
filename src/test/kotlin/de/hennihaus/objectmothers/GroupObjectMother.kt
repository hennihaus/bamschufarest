package de.hennihaus.objectmothers

import de.hennihaus.models.Group

object GroupObjectMother {

    const val DEFAULT_SCHUFA_BANK_NAME = "schufa"
    const val DEFAULT_BANK_NAME = "vbank"
    const val DEFAULT_JMS_BANK_A_NAME = "jmsBankA"

    const val DEFAULT_PASSWORD = "0123456789"
    const val DEFAULT_HAS_PASSED = false

    const val FIRST_GROUP_ID = "61320a79410347e41dbea0f9"
    const val FIRST_GROUP_USERNAME = "LoanBrokerGruppe01"
    const val FIRST_GROUP_JMS_QUEUE = "ResponseLoanBrokerGruppe01"
    const val SECOND_GROUP_ID = "61320a84befcde533be505c5"
    const val SECOND_GROUP_USERNAME = "LoanBrokerGruppe02"
    const val SECOND_GROUP_JMS_QUEUE = "ResponseLoanBrokerGruppe02"
    const val THIRD_GROUP_ID = "62449e3d944f2af727e6f1fb"
    const val THIRD_GROUP_USERNAME = "LoanBrokerGruppe03"
    const val THIRD_GROUP_JMS_QUEUE = "ResponseLoanBrokerGruppe03"

    fun getFirstGroup(
        id: String = FIRST_GROUP_ID,
        username: String = FIRST_GROUP_USERNAME,
        password: String = DEFAULT_PASSWORD,
        jmsQueue: String = FIRST_GROUP_JMS_QUEUE,
        students: List<String> = getDefaultStudents(),
        stats: Map<String, Int> = getDefaultStats(),
        hasPassed: Boolean = DEFAULT_HAS_PASSED,
    ) = Group(
        id = id,
        username = username,
        password = password,
        jmsQueue = jmsQueue,
        students = students,
        stats = stats,
        hasPassed = hasPassed,
    )

    fun getSecondGroup(
        id: String = SECOND_GROUP_ID,
        username: String = SECOND_GROUP_USERNAME,
        password: String = DEFAULT_PASSWORD,
        jmsQueue: String = SECOND_GROUP_JMS_QUEUE,
        students: List<String> = getDefaultStudents(),
        stats: Map<String, Int> = getDefaultStats(),
        hasPassed: Boolean = DEFAULT_HAS_PASSED,
    ) = Group(
        id = id,
        username = username,
        password = password,
        jmsQueue = jmsQueue,
        students = students,
        stats = stats,
        hasPassed = hasPassed,
    )

    fun getThirdGroup(
        id: String = THIRD_GROUP_ID,
        username: String = THIRD_GROUP_USERNAME,
        password: String = DEFAULT_PASSWORD,
        jmsQueue: String = THIRD_GROUP_JMS_QUEUE,
        students: List<String> = getDefaultStudents(),
        stats: Map<String, Int> = getDefaultStats(),
        hasPassed: Boolean = DEFAULT_HAS_PASSED,
    ) = Group(
        id = id,
        username = username,
        password = password,
        jmsQueue = jmsQueue,
        students = students,
        stats = stats,
        hasPassed = hasPassed,
    )

    private fun getDefaultStudents() = listOf(
        "Angelar Merkel",
        "Max Mustermann",
        "Thomas Müller",
    )

    private fun getDefaultStats() = mapOf(
        DEFAULT_SCHUFA_BANK_NAME to 0,
        DEFAULT_BANK_NAME to 0,
        DEFAULT_JMS_BANK_A_NAME to 0,
    )
}
