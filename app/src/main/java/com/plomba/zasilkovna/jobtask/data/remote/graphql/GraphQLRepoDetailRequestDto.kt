package com.plomba.zasilkovna.jobtask.data.remote.graphql

import com.plomba.zasilkovna.jobtask.common.OwnerNameVariable


data class GraphQLRepoDetailRequestDto(
    val operationName: String,
    val query: String,
    val variables: OwnerNameVariable
)