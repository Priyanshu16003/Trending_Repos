package com.example.trendingrepos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trendingrepos.model.Contributor
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.networking.GitHubRepoApi

class GitHubProjectRepository(val gitHubRepoApi: GitHubRepoApi) {
    private val repoListLiveData = MutableLiveData<Repos>()
    val repos : LiveData<Repos>
        get() = repoListLiveData

    suspend fun getTrendingRepos(){
        val response = gitHubRepoApi.getTrendingRepositories("watchers")
        if (response.body() != null){
            repoListLiveData.postValue(response.body())
        }
    }

}