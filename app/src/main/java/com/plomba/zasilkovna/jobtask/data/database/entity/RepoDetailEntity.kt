package com.plomba.zasilkovna.jobtask.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plomba.zasilkovna.jobtask.domain.model.RepoDetail
import com.plomba.zasilkovna.jobtask.domain.model.RepoListItem

@Entity(tableName = "repo_details")
data class RepoDetailEntity (
    val createdAt: String?,
    val diskUsage: Int?,
    val forkingAllowed: Boolean,
    @PrimaryKey val id: String,
    val languages: String?,
    val name: String,
    val owner: String,
    val readme: String?,
    val stargazerCount: Int,
    val updatedAt: String,
    val viewerHasStarred: Boolean
)

fun RepoDetailEntity.toRepoDetail():RepoDetail{
    return RepoDetail(
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