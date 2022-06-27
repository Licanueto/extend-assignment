package com.paywithextend.assignment.clients

import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import reactor.core.publisher.Mono

fun logRequestAndResponse(exchangeFilterFunctionList:MutableList<ExchangeFilterFunction>):Boolean =
       exchangeFilterFunctionList.add(logRequest())
    && exchangeFilterFunctionList.add(logResponse())

fun logRequest(): ExchangeFilterFunction = ExchangeFilterFunction.ofRequestProcessor{ clientRequest -> Mono.just(
    clientRequest.also { it.headers().forEach{ name, values ->
            values.forEach{ value -> print("\nRequest | name:$name value:$value") } } } ) }

fun logResponse(): ExchangeFilterFunction = ExchangeFilterFunction.ofResponseProcessor{ clientResponse -> Mono.just(
    clientResponse.also { it.headers().asHttpHeaders().forEach{ name, values ->
        values.forEach{ value -> print("\nResponse | name:$name value:$value") } } } ) }

fun Long?.toDollars():Double = this?.div(100.0) ?: 0.0

fun String?.defaultIfNull():String = this ?: "UNKNOWN"
