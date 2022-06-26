package com.paywithextend.assignment.clients

import com.paywithextend.assignment.mappers.toVirtualCardModelList
import com.paywithextend.assignment.models.UserModel
import com.paywithextend.assignment.models.VirtualCardModel
import com.paywithextend.assignment.responses.VirtualCardsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec
import reactor.core.publisher.Mono

@Component
class VirtualCardsClient (
    webClientBuilder: WebClient.Builder,
    private val URI: String = "/virtualcards",
    @Value("\${extend.baseUrl}") BASE_URL: String,
    @Value("\${extend.apiVersion}") private val API_VERSION: String
) {
    private val webClient:WebClient = webClientBuilder
//      .filters(::logRequestAndResponse)
        .baseUrl(BASE_URL)
        .build()

    fun getVirtualCards(user: UserModel): Mono<List<VirtualCardModel>> = webClient
        .get()
        .uri(URI)
        .buildHeaders(user)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError) { Mono.error(RuntimeException("Client Error ${it.statusCode()}")) }
        .onStatus(HttpStatus::is5xxServerError) { Mono.error(RuntimeException("Server Error ${it.statusCode()}")) }
        .bodyToMono(VirtualCardsResponse::class.java)
        .map { it.toVirtualCardModelList() }

    fun RequestHeadersSpec<*>.buildHeaders(user: UserModel): RequestHeadersSpec<*> = this
        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .header("Accept", API_VERSION)
        .header("Authorization", "Bearer ${user.extendToken?.value}")
}
