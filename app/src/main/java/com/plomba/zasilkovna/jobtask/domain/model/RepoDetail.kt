package com.plomba.zasilkovna.jobtask.domain.model

import com.plomba.zasilkovna.jobtask.data.database.entity.RepoDetailEntity

data class RepoDetail(
    val createdAt: String?,
    val diskUsage: Int?,
    val forkingAllowed: Boolean,
    val id: String,
    val languages: String?,
    val name: String,
    val owner: String,
    val readme: String?,
    val stargazerCount: Int,
    val updatedAt: String,
    val viewerHasStarred: Boolean
)

fun RepoDetail.toRepoDetailEntity():RepoDetailEntity{
    return RepoDetailEntity(
        createdAt,
        diskUsage,
        forkingAllowed,
        id,
        languages,
        name,
        owner,
        readme,
        stargazerCount,
        updatedAt,
        viewerHasStarred
    )
}