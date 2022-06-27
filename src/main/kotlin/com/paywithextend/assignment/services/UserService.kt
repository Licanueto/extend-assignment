package com.paywithextend.assignment.services

import com.paywithextend.assignment.clients.SignInClient
import com.paywithextend.assignment.models.UserModel
import com.paywithextend.assignment.repositories.UserRepositoryInterface
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Instant
import java.util.UUID

@Service
class UserService (
    private val signInClient:SignInClient,
    private val userRepository: UserRepositoryInterface,
    @Value("\${extend.tokenTimeoutMinutes}") private val TOKEN_TIMEOUT: Int
) {

    fun addUser(user: UserModel): Mono<Boolean> = userRepository.exists(user.id)
            .filter { alreadyExists -> !alreadyExists }
            .flatMap { signInClient.signIn(user) }
            .flatMap { userRepository.add(it) }
            .switchIfEmpty(errorResponseBoolean(HttpStatus.BAD_REQUEST))

    fun getSignedInUser(userId: UUID): Mono<UserModel> = userRepository.findById(userId)
            .switchIfEmpty { errorResponseUserModel(HttpStatus.NOT_FOUND) }
            .flatMap { user ->
                if (user.tokenIsStillValid()) Mono.just(user)
                else signInClient.signIn(user)          // TODO: I shouldn't have to wait for DB update
                                 .flatMap { updatedUser -> userRepository.replace(updatedUser).map { updatedUser } } }

    fun replaceUser(user: UserModel): Mono<Boolean> = userRepository.exists(user.id)
            .filter { it }
            .switchIfEmpty{ errorResponseBoolean(HttpStatus.NOT_FOUND) }
            .flatMap { signInClient.signIn(user) }
            .flatMap { userRepository.replace(it) }

    fun removeUser(userId: UUID): Mono<Boolean> = userRepository.remove(userId)
            .filter{ it }
            .switchIfEmpty { errorResponseBoolean(HttpStatus.NOT_FOUND) }

    fun UserModel.tokenIsStillValid(): Boolean = this
            .extendToken?.lastUpdated?.isAfter(Instant.now().minusSeconds((TOKEN_TIMEOUT * 60) - 8L )) ?: false
}
