package com.paywithextend.assignment.services

import com.paywithextend.assignment.models.UserModel
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

// TODO: Either turn this into something less redundant and more extensible, or remove it. (generic?)
//  Why? Response messages are tightly coupled to statuses.
//  I'm Leaving it as it is for now just because it improves readability.

fun errorResponseUserModel(httpStatus: HttpStatus, errorMessage: String? = null): Mono<UserModel> = Mono.error(
        ResponseStatusException(httpStatus, errorMessage ?: getMessage(httpStatus)))

fun errorResponseBoolean(httpStatus: HttpStatus, errorMessage: String? = null): Mono<Boolean> = Mono.error(
        ResponseStatusException(httpStatus, errorMessage ?: getMessage(httpStatus)))

fun errorResponseThrowable(httpStatus: HttpStatus, errorMessage: String? = null): Mono<Throwable> = Mono.error(
        ResponseStatusException(httpStatus, errorMessage ?: getMessage(httpStatus)))

private fun getMessage(httpStatus: HttpStatus): String = when (httpStatus) {
        HttpStatus.NOT_FOUND -> "User was not found"
        HttpStatus.BAD_REQUEST -> "User already exists. Trying to update it? Consider making a PUT request"
        HttpStatus.UNAUTHORIZED -> "Wrong email or password"
        else -> {"There was an error processing the request"} }
