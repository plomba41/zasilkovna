package com.plomba.zasilkovna.jobtask.data.repository

import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.composeJsonRequest
import com.apollographql.apollo3.api.json.buildJsonString
import com.apollographql.apollo3.api.parseJsonResponse
import com.google.gson.Gson
import com.plomba.zasilkovna.jobtask.AddStarMutation
import com.plomba.zasilkovna.jobtask.RemoveStarMutation
import com.plomba.zasilkovna.jobtask.RepoDetailsQuery
import com.plomba.zasilkovna.jobtask.data.GitHubService
import com.plomba.zasilkovna.jobtask.data.database.dao.RepoDetailDao
import com.plomba.zasilkovna.jobtask.data.database.entity.RepoDetailEntity
import com.plomba.zasilkovna.jobtask.data.remote.rest.RestRepoDto
import com.plomba.zasilkovna.jobtask.domain.repository.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl  @Inject constructor(
    val service: GitHubService,
    val repoDetailDao: RepoDetailDao
): GitHubRepository {
    override suspend fun getAllRepos(since: Int): List<RestRepoDto> {
        return service.getReposRest(since)
    }

    override suspend fun getRepoDetail(ownerName: String, repoName: String): RepoDetailsQuery.Data? {
        val query = RepoDetailsQuery(ownerName, repoName)
        val responseJson = service.getRepoDetails(
            prepareRequestJson(query)
        )
        val responseData = query.parseJsonResponse(responseJson.string())
        return responseData.data
    }

    override suspend fun starRepo(repoId: String) {
        service.mutateStar(
            prepareRequestJson(AddStarMutation(repoId))
        )
    }

    override suspend fun unstarRepo(repoId: String) {
        service.mutateStar(
            prepareRequestJson(RemoveStarMutation(repoId))
        )
    }

    private inline fun <T: Operation.Data, reified C> prepareRequestJson(operation: Operation<T>):C{
        val body = buildJsonString {
            operation.composeJsonRequest(this)
        }
        val gson = Gson()
        val request:C = gson.fromJson(body, C::class.java)
        return request
    }

    override suspend fun getAllLocalRepositories(): List<RepoDetailEntity> {
        return repoDetailDao.getAll()
    }

    override suspend fun saveRepositoryLocaly(repoDetail: RepoDetailEntity): Long {
        return repoDetailDao.upsert(repoDetail)
    }

    override suspend fun removeLocalRepository(repoDetail: RepoDetailEntity): Int {
        return repoDetailDao.delete(repoDetail)
    }

    override suspend fun retrieveLocalRepository(repoId: String): RepoDetailEntity {
        return repoDetailDao.getSingle(repoId)
    }
}