package com.paywithextend.assignment.controllers

import com.paywithextend.assignment.models.TransactionModel
import com.paywithextend.assignment.models.VirtualCardModel
import com.paywithextend.assignment.services.VirtualCardsService
import org.junit.jupiter.api.Test

import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.util.UUID

@WebFluxTest
internal class VirtualCardsControllerTest {

    @Autowired lateinit var client: WebTestClient

    @MockBean lateinit var virtualCardsService: VirtualCardsService

    private val userId = UUID.randomUUID()

    @Test
    fun getVirtualCardForUserId() {
        val serviceResponse: Mono<List<VirtualCardModel>> = Mono.just(getVirtualCardModelList())

        Mockito.`when`(virtualCardsService.getCardsForUserId(userId))
            .thenReturn(serviceResponse)

        val expectedResponse: String = getListAsJsonString()

        client
            .get()
            .uri("/virtualcards/$userId")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectBody().json(expectedResponse)

    }

    private fun getVirtualCardModelList():List<VirtualCardModel> = listOf(
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

    private fun getListAsJsonString(): String = """
         [
             {
                 "id": "vc_1M5uLzsFPHA99t5nef1dd6",
                 "displayName": "Some card",
                 "creditCardDisplayName": "Same card rephrased",
                 "expires": "1 of Aug 2023",
                 "status": "ACTIVE",
                 "limit": 2000.0,
                 "balance": 1200.0,
                 "spent": 800.0,
                 "issuerName": "Comdata",
                 "address": "63 Downing St #8B  New York NY 10014 US",
                 "cardHolder": {
                     "name": "Wilbert Trane",
                     "email": "wilbert.trane@paywithextend.com"
                 },
                 "cardRecipient": {
                     "name": "Mickie Churley",
                     "email": "mickie.churley@paywithextend.com"
                 },
                 "transactions": [
                     {
                         "id": "txn_Ak8eFDBlnGO8OqpxyqL0tB",
                         "status": "PENDING",
                         "approvalCode": "550851",
                         "amount": 570.0,
                         "currency": "USD",
                         "merchant": "DELTA AIR 2135 651235",
                         "group": "PROFESSIONAL",
                         "attachmentsAmount": 2
                     },
                     {
                         "id": "txn_9hppBufUxLAAxH0rhXKXLu",
                         "status": "PENDING",
                         "approvalCode": "550757",
                         "amount": 230.0,
                         "currency": "USD",
                         "merchant": "AMZ*AKE ST 54537",
                         "group": "RETAIL",
                         "attachmentsAmount": 1
                     }
                 ]
             },
             {
                 "id": "vc_2n6v7sznHP488TshEFibDg",
                 "displayName": "Another card",
                 "creditCardDisplayName": "Another rephrased card",
                 "expires": "22 of Sep 2024",
                 "status": "SUSPENDED",
                 "limit": 3300.0,
                 "balance": 700.0,
                 "spent": 2600.0,
                 "issuerName": "Comdata",
                 "address": "36 Upping St #4A  New Jersey NJ 17063 US",
                 "cardHolder": {
                     "name": "Wilbert Trane",
                     "email": "wilbert.trane@paywithextend.com"
                 },
                 "cardRecipient": {
                     "name": "Mickie Churley",
                     "email": "mickie.churley@paywithextend.com"
                 },
                 "transactions": [
                     {
                         "id": "txn_Ak8eFDBlnGO8OblxyqL0tB",
                         "status": "PENDING",
                         "approvalCode": "570832",
                         "amount": 1750.0,
                         "currency": "USD",
                         "merchant": "AMERICAN AIRL 954",
                         "group": "PROFESSIONAL",
                         "attachmentsAmount": 2
                     },
                     {
                         "id": "txn_Ak8eFDBlnGO8Oasdqjatv",
                         "status": "PENDING",
                         "approvalCode": "531285",
                         "amount": 80.0,
                         "currency": "USD",
                         "merchant": "BROER BAKERY *SE4G",
                         "group": "RETAIL",
                         "attachmentsAmount": 1
                     },
                     {
                         "id": "txn_9hppBufUxLAAxH0rhdsaLu",
                         "status": "APPROVED",
                         "approvalCode": "658423",
                         "amount": 770.0,
                         "currency": "USD",
                         "merchant": "AMZ*SAM TR 56784",
                         "group": "RETAIL",
                         "attachmentsAmount": 0
                     }
                 ]
             }
         ]
    """.trimIndent()
}
