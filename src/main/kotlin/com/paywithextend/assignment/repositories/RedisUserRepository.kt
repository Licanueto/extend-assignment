package com.paywithextend.assignment.repositories

import com.paywithextend.assignment.models.UserModel
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class RedisUserRepository (
    private val operations: ReactiveRedisOperations<UUID, UserModel>
) : UserRepositoryInterface {

    override fun add(user: UserModel): Mono<Boolean> = operations.opsForValue().setIfAbsent(user.id, user)

    override fun findById(userId: UUID): Mono<UserModel> = operations.opsForValue().get(userId)

    // TODO: Improve this if it's going to be used with a frequency any higher than never
    override fun getAllUsers(): Mono<List<UserModel>> = getAllUserIds().flatMap { findById(it) }.collectList()

    override fun remove(userId: UUID): Mono<Boolean> = operations.opsForValue().delete(userId)

    override fun replace(user: UserModel): Mono<Boolean> = operations.opsForValue().setIfPresent(user.id, user)

    override fun exists(userId: UUID): Mono<Boolean> = operations.hasKey(userId)

    private fun getAllUserIds(): Flux<UUID> = operations.scan()

}
