package com.paywithextend.assignment.models

data class TransactionModel (
    val id: String,
    val status: String,
    val approvalCode: String,
    val amount: Double,
    val currency: String,
    val merchant: String,
    val group: String,
    val attachmentsAmount: Int
)
