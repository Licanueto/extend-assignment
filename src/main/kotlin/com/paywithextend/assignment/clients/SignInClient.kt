package com.paywithextend.assignment.clients

import com.paywithextend.assignment.models.UserModel
import com.paywithextend.assignment.responses.SignInResponse
import com.paywithextend.assignment.services.errorResponseThrowable
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec
import reactor.core.publisher.Mono
import java.time.Instant

@Component
class SignInClient (
    webClientBuilder: WebClient.Builder,
    private val URI: String = "/signin",
    @Value("\${extend.baseUrl}") BASE_URL: String,
    @Value("\${extend.apiVersion}") private val API_VERSION: String,
) {
    private val webClient:WebClient = webClientBuilder
//      .filters(::logRequestAndResponse)
        .baseUrl(BASE_URL)
        .build()

    fun signIn(user: UserModel): Mono<UserModel> = webClient
        .post()
        .uri(URI)
        .buildBody(user)
        .buildHeaders()
        .retrieve()
        .onStatus(HttpStatus.FORBIDDEN::equals) { errorResponseThrowable(HttpStatus.UNAUTHORIZED) }
        .onStatus(HttpStatus::is4xxClientError) { Mono.error(RuntimeException("Client Error ${it.statusCode()}")) }
        .onStatus(HttpStatus::is5xxServerError) { Mono.error(RuntimeException("Server Error ${it.statusCode()}")) }
        .bodyToMono(SignInResponse::class.java)
        .map { signInResponse -> user.updateCredentials(signInResponse.token) }

    fun RequestBodySpec.buildBody(user:UserModel) = body(
        Mono.just(LoginBody(user.extendEmail, user.extendPassword)), LoginBody::class.java)

    fun RequestHeadersSpec<*>.buildHeaders(): RequestHeadersSpec<*> = this
        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .header("Accept", API_VERSION)

    private data class LoginBody(val email: String, val password: String)

    private fun UserModel.updateCredentials(newToken: String): UserModel = this
        .copy(extendToken = UserModel.ExtendToken(value = newToken, lastUpdated = Instant.now()))
}
