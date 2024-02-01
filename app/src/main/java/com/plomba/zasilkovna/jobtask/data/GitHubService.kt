package com.plomba.zasilkovna.jobtask.data

import com.plomba.zasilkovna.jobtask.data.remote.graphql.GraphQLRepoDetailRequestDto
import com.plomba.zasilkovna.jobtask.data.remote.graphql.GraphQLStarrableRequestDto
import com.plomba.zasilkovna.jobtask.data.remote.rest.RestRepoDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GitHubService {
    @GET("repositories")
    suspend fun getReposRest(
        @Query("since") since: Int
    ): List<RestRepoDto>

    @POST("graphql")
    suspend fun getRepoDetails(
        @Body body: GraphQLRepoDetailRequestDto
    ): ResponseBody

    @POST("graphql")
    suspend fun mutateStar(
        @Body body: GraphQLStarrableRequestDto
    ): ResponseBody
}
