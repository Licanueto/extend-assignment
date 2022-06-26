package com.paywithextend.assignment.responses

data class VirtualCardsResponse (
    val pagination: Pagination?,
    val virtualCards: List<VirtualCards>?
) {

    data class Pagination(
        val page: Int?,
        val pageItemCount: Int?,
        val totalItems: Int?,
        val numberOfPages: Int?
    )

    data class Recipient(
        val id: String?,
        val firstName: String?,
        val lastName: String?,
        val email: String?,
        val phone: String?,
        val phoneIsoCountry: String?,
        val avatarType: String?,
        val avatarUrl: String?,
        val createdAt: String?,
        val updatedAt: String?,
        val currency: String?,
        val locale: String?,
        val timezone: String?,
        val verified: Boolean?,
        val hasExpensifyLink: Boolean?,
        val quickbooksTokenId: String?,
        val employeeId: String?,
        val issuerSanctions: ArrayList<IssuerSanctions>?,
        val organization: Organization?,
        val organizationId: String?,
        val organizationRole: String?
    )

    data class IssuerSanctions(
        val name: String?,
        val status: String?
    )

    data class Organization(
        val id: String?,
        val role: String?,
        val joinedAt: String?,
        val lastLoggedInAt: String?,
        val explicit: Boolean?
    )

    data class Cardholder(
        val id: String?,
        val firstName: String?,
        val lastName: String?,
        val email: String?,
        val phone: String?,
        val phoneIsoCountry: String?,
        val avatarType: String?,
        val avatarUrl: String?,
        val createdAt: String?,
        val updatedAt: String?,
        val currency: String?,
        val locale: String?,
        val timezone: String?,
        val verified: Boolean?,
        val hasExpensifyLink: Boolean?,
        val quickbooksTokenId: String?,
        val employeeId: String?,
        val issuerSanctions: List<IssuerSanctions>?,
        val organization: Organization?,
        val organizationId: String?,
        val organizationRole: String?
    )

    data class Urls(
        val property1: String?,
        val property2: String?
    )

    data class CardImage(
        val id: String?,
        val contentType: String?,
        val urls: Urls?,
        val textColorRGBA: String?,
        val hasTextShadow: Boolean?,
        val shadowTextColorRGBA: String?
    )

    data class Recurrence(
        val id: String?,
        val balanceCents: Long?,
        val period: String?,
        val interval: Int?,
        val terminator: String?,
        val count: Int?,
        val until: String?,
        val byWeekDay: Int?,
        val byMonthDay: Int?,
        val byYearDay: Int?,
        val prevRecurrenceAt: String?,
        val nextRecurrenceAt: String?,
        val currentCount: Int?,
        val remainingCount: Int?,
        val createdAt: String?,
        val updatedAt: String?
    )

    data class Pending(
        val balanceCents: Long?,
        val validFrom: String?,
        val validTo: String?,
        val recurs: Boolean?,
        val activeUntil: String?,
        val currency: String?,
        val recurrence: Recurrence?,
        val receiptAttachments: Any?
    )

    data class Address(
        val address1: String?,
        val address2: String?,
        val city: String?,
        val province: String?,
        val postal: String?,
        val country: String?
    ) {
        override fun toString(): String = "$address1 $address2 $city $province $postal $country"
    }

    data class Features(
        val recurrence: Boolean?,
        val customAddress: Boolean?,
        val customMin: Boolean?,
        val customMax: Boolean?,
        val walletsEnabled: String?,
        val mccControl: Boolean?,
        val qboReportEnabled: Boolean?
    )

    data class ValidMccRanges(
        val lowest: Int?,
        val highest: Int?
    )

    data class VirtualCards(
        val id: String?,
        val status: String?,
        val recipientId: String?,
        val recipient: Recipient?,
        val cardholderId: String?,
        val cardholder: Cardholder?,
        val cardImage: CardImage?,
        val displayName: String?,
        val expires: String?,
        val currency: String?,
        val limitCents: Long?,
        val balanceCents: Long?,
        val spentCents: Long?,
        val lifetimeSpentCents: Long?,
        val awaitingBudget: Boolean?,
        val last4: String?,
        val numberFormat: String?,
        val validFrom: String?,
        val validTo: String?,
        val inactiveSince: String?,
        val timezone: String?,
        val creditCardId: String?,
        val recurs: Boolean?,
        val recurrence: Recurrence?,
        val pending: Pending?,
        val notes: String?,
        val createdAt: String?,
        val updatedAt: String?,
        val address: Address?,
        val direct: Boolean?,
        val features: Features?,
        val activeUntil: String?,
        val minTransactionCents: Long?,
        val maxTransactionCents: Long?,
        val maxTransactionCount: Int?,
        val tokenReferenceIds: String?,
        val network: String?,
        val companyName: String?,
        val creditCardDisplayName: String?,
        val issuer: Issuer?,
        val validMccRanges: ValidMccRanges?
    )

    data class Issuer(
        val id: String?,
        val name: String?
    )

}
