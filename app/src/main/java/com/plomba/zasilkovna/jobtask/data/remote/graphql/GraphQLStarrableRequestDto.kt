package com.plomba.zasilkovna.jobtask.data.remote.graphql


data class GraphQLStarrableRequestDto(
    val operationName: String,
    val query: String,
    val variables: Starrable
)