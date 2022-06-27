package com.paywithextend.assignment.mappers

import com.paywithextend.assignment.clients.defaultIfNull
import com.paywithextend.assignment.clients.toDollars
import com.paywithextend.assignment.models.TransactionModel
import com.paywithextend.assignment.models.VirtualCardModel
import com.paywithextend.assignment.responses.TransactionsResponse
import com.paywithextend.assignment.responses.VirtualCardsResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun VirtualCardsResponse.toVirtualCardModelList():List<VirtualCardModel> = virtualCards?.map { card ->
    VirtualCardModel(id = card.id.defaultIfNull(),
        displayName = card.displayName.defaultIfNull(),
        creditCardDisplayName = card.creditCardDisplayName.defaultIfNull(),
        expires = if(card.expires != null)
            LocalDate.parse(card.expires, formatterFromTheirs).format(formatterToOurs) else "UNKNOWN",
        status = card.status.defaultIfNull(),
        limit = card.limitCents.toDollars(),
        balance = card.balanceCents.toDollars(),
        spent = card.spentCents.toDollars(),
        issuerName = card.issuer?.name.defaultIfNull(),
        address = card.address.toString(),
        cardHolder = VirtualCardModel.Person(
            name = card.cardholder?.firstName + " " + card.cardholder?.lastName.defaultIfNull(),
            email = card.cardholder?.email.defaultIfNull()),
        cardRecipient = VirtualCardModel.Person(
            name = card.recipient?.firstName + " " + card.recipient?.lastName.defaultIfNull(),
            email = card.recipient?.email.defaultIfNull()),
        transactions = emptyList<TransactionModel>())
} ?: emptyList<VirtualCardModel>()

fun TransactionsResponse.toTransactionModelList():List<TransactionModel> = transactions?.map { transaction ->
    TransactionModel(id = transaction.id.defaultIfNull(),
        status = transaction.status.defaultIfNull(),
        approvalCode = transaction.approvalCode.defaultIfNull(),
        amount = transaction.authBillingAmountCents.toDollars(),
        currency = transaction.clearingBillingCurrency.defaultIfNull(),
        merchant = transaction.merchantName.defaultIfNull(),
        group = transaction.mccGroup.defaultIfNull(),
        attachmentsAmount = transaction.attachmentsCount ?: 0 )
} ?: emptyList<TransactionModel>()

private val formatterFromTheirs = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
private val formatterToOurs = DateTimeFormatter.ofPattern("d' of 'LLL' 'uuuu", Locale.US)
