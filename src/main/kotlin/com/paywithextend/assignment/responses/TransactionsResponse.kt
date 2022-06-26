package com.paywithextend.assignment.responses

data class TransactionsResponse (
    val transactions: List<Transaction>?
) {

    data class DeclineReasons (
        val code: String?,
        val description: String?
    )
    
    data class ReferenceFields (
        val fieldLabel: String?,
        val fieldCode: String?,
        val optionLabel: String?,
        val optionCode: String?
    )

    data class Transaction (
        val id: String?,
        val cardholderId: String?,
        val cardholderName: String?,
        val cardholderEmail: String?,
        val recipientName: String?,
        val recipientEmail: String?,
        val recipientId: String?,
        val nameOnCard: String?,
        val source: String?,
        val vcnLast4: String?,
        val vcnDisplayName: String?,
        val virtualCardId: String?,
        val type: String?,
        val status: String?,
        val declineReasons: ArrayList<DeclineReasons>?,
        val approvalCode: String?,
        val authBillingAmountCents: Long?,
        val authBillingCurrency: String?,
        val authMerchantAmountCents: Long?,
        val authMerchantCurrency: String?,
        val authExchangeRate: Double?,
        val clearingBillingAmountCents: Long?,
        val clearingBillingCurrency: String?,
        val clearingMerchantAmountCents: Long?,
        val clearingMerchantCurrency: String?,
        val clearingExchangeRate: Double?,
        val mcc: String?,
        val mccGroup: String?,
        val mccDescription: String?,
        val merchantId: String?,
        val merchantName: String?,
        val merchantAddress: String?,
        val merchantCity: String?,
        val merchantState: String?,
        val merchantCountry: String?,
        val merchantZip: String?,
        val authedAt: String?,
        val clearedAt: String?,
        val updatedAt: String?,
        val hasAttachments: Boolean?,
        val referenceId: String?,
        val creditCardId: String?,
        val sentToExpensify: Boolean?,
        val sentToQuickbooks: Boolean?,
        val attachmentsCount: Int?,
        val referenceFields: ArrayList<ReferenceFields>?,
        val creditCardDisplayName: String?
    )

}
