package com.paywithextend.assignment.models

import java.time.Instant
import java.util.UUID

data class UserModel (
    val id: UUID,
    val extendEmail: String,
    val extendPassword: String,
    var extendToken: ExtendToken?
) {

    data class ExtendToken(
        val lastUpdated: Instant = Instant.now(),
        val value:String
    )
}
