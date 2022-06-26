package com.paywithextend.assignment.responses

data class SignInResponse (
    val user: UserSignInResponse? = null,
    val token: String,
    val refreshToken: String? = null
) {

    data class UserSignInResponse(
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
        val issuerSanctions: List<IssuerSanctionsSignInResponse>?,
        val organization: OrganizationSignInResponse?,
        val organizationId: String?,
        val organizationRole: String?
    )

    data class IssuerSanctionsSignInResponse(
        val name: String?,
        val status: String?
    )

    data class OrganizationSignInResponse(
        val id: String?,
        val role: String?,
        val joinedAt: String?,
        val lastLoggedInAt: String?,
        val explicit: Boolean?
    )
}
