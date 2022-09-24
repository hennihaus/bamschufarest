package de.hennihaus.services.callservices

import de.hennihaus.bamdatamodel.Bank
import de.hennihaus.configurations.Configuration.BANK_UUID
import de.hennihaus.plugins.initKoin
import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import java.util.UUID

@Disabled(value = "until dev cluster is available")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankCallServiceIntegrationTest : KoinTest {

    private val classUnderTest: BankCallService by inject()

    @JvmField
    @RegisterExtension
    @Suppress("unused")
    val koinTestInstance = KoinTestExtension.create {
        initKoin()
    }

    @AfterAll
    fun cleanUp() = stopKoin()

    @Nested
    inner class GetBankById {
        @Test
        fun `should return a bank by id`() = runBlocking<Unit> {
            val id = UUID.fromString(getKoin().getProperty(key = BANK_UUID))

            val result: Bank = classUnderTest.getBankById(
                id = id,
            )

            result.shouldNotBeNull()
        }
    }
}
