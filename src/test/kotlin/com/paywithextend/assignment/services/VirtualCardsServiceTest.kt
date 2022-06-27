package com.paywithextend.assignment.services

import com.paywithextend.assignment.clients.TransactionsClient
import com.paywithextend.assignment.clients.VirtualCardsClient
import com.paywithextend.assignment.models.TransactionModel
import com.paywithextend.assignment.models.UserModel
import com.paywithextend.assignment.models.VirtualCardModel
import org.junit.jupiter.api.Test

import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant
import java.util.UUID

@SpringBootTest
internal class VirtualCardsServiceTest {

    @Autowired private lateinit var virtualCardsService: VirtualCardsService

    @MockBean private lateinit var userService: UserService

    @MockBean private lateinit var virtualCardsClient: VirtualCardsClient

    @MockBean private lateinit var transactionsClient: TransactionsClient

    private val now: Instant = Instant.now()

    private val userId = UUID.randomUUID()

    @Test
    fun getCardsForUserId() {
        val user = UserModel(id = userId, extendEmail = "name.sur@aol.com", extendPassword = "p@ss",
            extendToken = UserModel.ExtendToken(lastUpdated = now, value = "abc.def.ghi"))

        Mockito.`when`(userService.getSignedInUser(userId))
               .thenReturn(Mono.just(user))
        Mockito.`when`(virtualCardsClient.getVirtualCards(user))
               .thenReturn(Mono.just(virtualCardModelListWithoutTransactions))
        Mockito.`when`(transactionsClient.getTransactions(user, virtualCardModelListWithoutTransactions[0]))
            .thenReturn(Mono.just(transactionList(virtualCardModelListWithoutTransactions[0].id)))
        Mockito.`when`(transactionsClient.getTransactions(user, virtualCardModelListWithoutTransactions[1]))
            .thenReturn(Mono.just(transactionList(virtualCardModelListWithoutTransactions[1].id)))

        StepVerifier.create(virtualCardsService.getCardsForUserId(userId))
            .expectNext(virtualCardModelListWithTransactions)
    }


    private val virtualCardModelListWithoutTransactions:List<VirtualCardModel> = listOf(
        VirtualCardModel(
            id = "vc_1M5uLzsFPHA99t5nef1dd6",
            displayName = "Some card",
            creditCardDisplayName = "Same card rephrased",
            expires = "1 of Aug 2023",
            status = "ACTIVE",
            limit = 2000.0,
            balance = 1200.0,
            spent = 800.0,
            issuerName = "Comdata",
            address = "63 Downing St #8B  New York NY 10014 US",
            cardHolder = VirtualCardModel.Person("Wilbert Trane", "wilbert.trane@paywithextend.com"),
            cardRecipient = VirtualCardModel.Person("Mickie Churley", "mickie.churley@paywithextend.com"),
            transactions = emptyList() ),
        VirtualCardModel(
            id = "vc_2n6v7sznHP488TshEFibDg",
            displayName = "Another card",
            creditCardDisplayName = "Another rephrased card",
            expires = "22 of Sep 2024",
            status = "SUSPENDED",
            limit = 3300.0,
            balance = 700.0,
            spent = 2600.0,
            issuerName = "Comdata",
            address = "36 Upping St #4A  New Jersey NJ 17063 US",
            cardHolder = VirtualCardModel.Person("Wilbert Trane", "wilbert.trane@paywithextend.com"),
            cardRecipient = VirtualCardModel.Person("Mickie Churley", "mickie.churley@paywithextend.com"),
            transactions = emptyList()))

    private fun transactionList(cardId: String): List<TransactionModel> = when(cardId) {
            "vc_1M5uLzsFPHA99t5nef1dd6" -> listOf(
                TransactionModel(
                    id = "txn_Ak8eFDBlnGO8OqpxyqL0tB",
                    status = "PENDING",
                    approvalCode = "550851",
                    amount = 570.0,
                    currency = "USD",
                    merchant = "DELTA AIR 2135 651235",
                    group = "PROFESSIONAL",
                    attachmentsAmount = 2 ),
                TransactionModel(
                    id = "txn_9hppBufUxLAAxH0rhXKXLu",
                    status = "PENDING",
                    approvalCode = "550757",
                    amount = 230.0,
                    currency = "USD",
                    merchant = "AMZ*AKE ST 54537",
                    group = "RETAIL",
                    attachmentsAmount = 1 ))
            "vc_2n6v7sznHP488TshEFibDg" -> listOf(
                TransactionModel(
                    id = "vc_2n6v7sznHP488TshEFibDg",
                    status = "PENDING",
                    approvalCode = "570832",
                    amount = 1750.0,
                    currency = "USD",
                    merchant = "AMERICAN AIRL 954",
                    group = "PROFESSIONAL",
                    attachmentsAmount = 2),
                TransactionModel(
                    id = "txn_Ak8eFDBlnGO8Oasdqjatv",
                    status = "PENDING",
                    approvalCode = "531285",
                    amount =80.0,
                    currency = "USD",
                    merchant = "BROER BAKERY *SE4G",
                    group = "RETAIL",
                    attachmentsAmount = 1 ),
                TransactionModel(
                    id = "txn_9hppBufUxLAAxH0rhdsaLu",
                    status = "APPROVED",
                    approvalCode = "658423",
                    amount = 770.0,
                    currency = "USD",
                    merchant = "AMZ*SAM TR 56784",
                    group = "RETAIL",
                    attachmentsAmount = 0 ))
            else -> emptyList() }

    private val virtualCardModelListWithTransactions:List<VirtualCardModel> = listOf(
        VirtualCardModel(
            id = "vc_1M5uLzsFPHA99t5nef1dd6",
            displayName = "Some card",
            creditCardDisplayName = "Same card rephrased",
            expires = "1 of Aug 2023",
            status = "ACTIVE",
            limit = 2000.0,
            balance = 1200.0,
            spent = 800.0,
            issuerName = "Comdata",
            address = "63 Downing St #8B  New York NY 10014 US",
            cardHolder = VirtualCardModel.Person("Wilbert Trane", "wilbert.trane@paywithextend.com"),
            cardRecipient = VirtualCardModel.Person("Mickie Churley", "mickie.churley@paywithextend.com"),
            transactions = listOf(
                TransactionModel(
                    id = "txn_Ak8eFDBlnGO8OqpxyqL0tB",
                    status = "PENDING",
                    approvalCode = "550851",
                    amount = 570.0,
                    currency = "USD",
                    merchant = "DELTA AIR 2135 651235",
                    group = "PROFESSIONAL",
                    attachmentsAmount = 2 ),
                TransactionModel(
                    id = "txn_9hppBufUxLAAxH0rhXKXLu",
                    status = "PENDING",
                    approvalCode = "550757",
                    amount = 230.0,
                    currency = "USD",
                    merchant = "AMZ*AKE ST 54537",
                    group = "RETAIL",
                    attachmentsAmount = 1 ))),
        VirtualCardModel(
            id = "vc_2n6v7sznHP488TshEFibDg",
            displayName = "Another card",
            creditCardDisplayName = "Another rephrased card",
            expires = "22 of Sep 2024",
            status = "SUSPENDED",
            limit = 3300.0,
            balance = 700.0,
            spent = 2600.0,
            issuerName = "Comdata",
            address = "36 Upping St #4A  New Jersey NJ 17063 US",
            cardHolder = VirtualCardModel.Person("Wilbert Trane", "wilbert.trane@paywithextend.com"),
            cardRecipient = VirtualCardModel.Person("Mickie Churley", "mickie.churley@paywithextend.com"),
            transactions = listOf(
                TransactionModel(
                    id = "txn_Ak8eFDBlnGO8OblxyqL0tB",
                    status = "PENDING",
                    approvalCode = "570832",
                    amount = 1750.0,
                    currency = "USD",
                    merchant = "AMERICAN AIRL 954",
                    group = "PROFESSIONAL",
                    attachmentsAmount = 2),
                TransactionModel(
                    id = "txn_Ak8eFDBlnGO8Oasdqjatv",
                    status = "PENDING",
                    approvalCode = "531285",
                    amount =80.0,
                    currency = "USD",
                    merchant = "BROER BAKERY *SE4G",
                    group = "RETAIL",
                    attachmentsAmount = 1 ),
                TransactionModel(
                    id = "txn_9hppBufUxLAAxH0rhdsaLu",
                    status = "APPROVED",
                    approvalCode = "658423",
                    amount = 770.0,
                    currency = "USD",
                    merchant = "AMZ*SAM TR 56784",
                    group = "RETAIL",
                    attachmentsAmount = 0 ))))
}
