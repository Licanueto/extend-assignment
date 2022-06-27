package com.paywithextend.assignment.controllers

import com.paywithextend.assignment.models.VirtualCardModel
import com.paywithextend.assignment.services.VirtualCardsService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
class VirtualCardsController(
    private val virtualCardsService: VirtualCardsService
) {

    @GetMapping(value = ["/virtualcards/{userId}"],
                produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getVirtualCardForUserId(@PathVariable userId: String): Mono<List<VirtualCardModel>> = virtualCardsService
            .getCardsForUserId(UUID.fromString(userId))
}
