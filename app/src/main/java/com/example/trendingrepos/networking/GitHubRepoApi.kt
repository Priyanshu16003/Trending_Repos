package com.example.trendingrepos.networking

import com.example.trendingrepos.model.Item
import com.example.trendingrepos.model.Repos
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubRepoApi {

    @GET("/search/repositories")
    suspend fun getTrendingRepositories(
        @Query("q") query: String
    ) : Response<Repos>
}