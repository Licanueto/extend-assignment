package com.paywithextend.assignment.models

data class VirtualCardModel (
    val id: String,
    val displayName: String,
    val creditCardDisplayName: String,
    val expires: String,
    val status: String,
    val limit: Double,
    val balance: Double,
    val spent: Double,
    val issuerName: String,
    val address: String,
    val cardHolder: Person,
    val cardRecipient: Person,
    var transactions: List<TransactionModel>
) {
    data class Person (
        val name: String,
        val email:String
    )

    fun updateTransactions(transactions:List<TransactionModel>):VirtualCardModel = this
        .also { it.transactions = transactions }
}
