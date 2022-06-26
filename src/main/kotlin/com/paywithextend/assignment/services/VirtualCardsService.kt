package com.paywithextend.assignment.services

import com.paywithextend.assignment.clients.TransactionsClient
import com.paywithextend.assignment.clients.VirtualCardsClient
import com.paywithextend.assignment.models.VirtualCardModel
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.util.UUID

@Service
class VirtualCardsService (
    private val userService: UserService,
    private val virtualCardsClient: VirtualCardsClient,
    private val transactionsClient: TransactionsClient
) {

    fun getCardsForUserId(userId: UUID): Mono<List<VirtualCardModel>> = userService.getSignedInUser(userId)
            .flatMap { user ->
                virtualCardsClient.getVirtualCards(user)
                    .flatMapMany { cards -> cards.toFlux() }
                    .flatMap { card -> transactionsClient
                        .getTransactions(user, card)
                        .map { transactions -> card.updateTransactions(transactions)
                            .also { println("The user with email ${user.extendEmail} has a '${card.issuerName}' card " +
                                    "with ${transactions.size} transactions") } } } // Removable / or replace with log
                    .collectList() }

}
