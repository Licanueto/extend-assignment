package com.paywithextend.assignment.clients

import com.paywithextend.assignment.mappers.toTransactionModelList
import com.paywithextend.assignment.models.TransactionModel
import com.paywithextend.assignment.models.UserModel
import com.paywithextend.assignment.models.VirtualCardModel
import com.paywithextend.assignment.responses.TransactionsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class TransactionsClient (
    webClientBuilder: WebClient.Builder,
    private val URI_FIRST: String = "/virtualcards/",
    private val URI_LAST: String = "/transactions",
    @Value("\${extend.baseUrl}") BASE_URL: String,
    @Value("\${extend.apiVersion}") private val API_VERSION: String
) {
    private val webClient: WebClient = webClientBuilder
//      .filters(::logRequestAndResponse)
        .baseUrl(BASE_URL)
        .build()

    fun getTransactions(user: UserModel, card: VirtualCardModel): Mono<List<TransactionModel>> = webClient
        .get()
        .uri(URI_FIRST + card.id + URI_LAST)
        .buildHeaders(user)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError) { Mono.error(RuntimeException("Client Error ${it.statusCode()}")) }
        .onStatus(HttpStatus::is5xxServerError) { Mono.error(RuntimeException("Server Error ${it.statusCode()}")) }
        .bodyToMono(TransactionsResponse::class.java)
        .doOnSuccess { println("\nTransaction Response Size: ${it.transactions?.size}") }
        .map { it.toTransactionModelList() }

    fun WebClient.RequestHeadersSpec<*>.buildHeaders(user: UserModel): WebClient.RequestHeadersSpec<*> = this
        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .header("Accept", API_VERSION)
        .header("Authorization", "Bearer ${user.extendToken?.value}")
}
