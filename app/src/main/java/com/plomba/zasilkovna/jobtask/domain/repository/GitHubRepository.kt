package com.plomba.zasilkovna.jobtask.domain.repository

import com.plomba.zasilkovna.jobtask.RepoDetailsQuery
import com.plomba.zasilkovna.jobtask.data.database.entity.RepoDetailEntity
import com.plomba.zasilkovna.jobtask.data.remote.rest.RestRepoDto
import com.plomba.zasilkovna.jobtask.domain.model.RepoDetail

interface GitHubRepository {
    suspend fun getAllRepos(since:Int): List<RestRepoDto>

    suspend fun getRepoDetail(ownerName: String, repoName:String): RepoDetailsQuery.Data?

    suspend fun starRepo(repoId: String)

    suspend fun unstarRepo(repoId: String)

    suspend fun getAllLocalRepositories(): List<RepoDetailEntity>

    suspend fun saveRepositoryLocaly(repoDetail: RepoDetailEntity): Long

    suspend fun removeLocalRepository(repoDetail: RepoDetailEntity): Int

    suspend fun retrieveLocalRepository(repoId: String): RepoDetailEntity
}