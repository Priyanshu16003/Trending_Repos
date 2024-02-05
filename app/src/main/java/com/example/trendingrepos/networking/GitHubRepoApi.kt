package com.example.trendingrepos.networking

import com.example.trendingrepos.model.Contributor
import com.example.trendingrepos.model.Repos
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubRepoApi {

    @GET("/search/repositories")
    suspend fun getTrendingRepositories(
        @Query("q") query: String
    ) : Response<Repos>

    @GET("/repos/{login}/{name}/contributors")
    suspend fun getContributorsAvatar(
        @Path("login") login: String,
        @Path("name") name: String
    ) : Response<List<Contributor>>
}