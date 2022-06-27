package com.paywithextend.assignment.repositories

import com.paywithextend.assignment.models.UserModel
import reactor.core.publisher.Mono
import java.util.UUID

interface UserRepositoryInterface {

    fun add(user: UserModel): Mono<Boolean>

    fun findById(userId:UUID): Mono<UserModel>

    fun exists(userId: UUID): Mono<Boolean>

    fun getAllUsers(): Mono<List<UserModel>>

    fun remove(userId: UUID): Mono<Boolean>

    fun replace(user: UserModel): Mono<Boolean>
}
