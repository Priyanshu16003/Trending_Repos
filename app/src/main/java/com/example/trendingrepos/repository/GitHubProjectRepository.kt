package com.example.trendingrepos.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trendingrepos.NetworkConnectivityObserver
import com.example.trendingrepos.db.ItemDatabase
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.networking.GitHubRepoApi
import com.example.trendingrepos.utils.NetworkUtils
import com.example.trendingrepos.utils.Resource

class GitHubProjectRepository(
    private val gitHubRepoApi: GitHubRepoApi,
    private val itemDatabase: ItemDatabase,
    private val applicationContext: Context
) {
    private val repoListLiveData = MutableLiveData<Resource<Repos>>()
    val repos : LiveData<Resource<Repos>>
        get() = repoListLiveData

    suspend fun getTrendingRepos(){
        repoListLiveData.postValue(Resource.LOADING(null))
        if(NetworkUtils.isInternetAvailable(applicationContext)){

            try {
                val response = gitHubRepoApi.getTrendingRepositories()
                if (response.body() != null){
                    itemDatabase.itemDao().deleteAll()
                    itemDatabase.itemDao().addItems(response.body()!!.items)
                    repoListLiveData.postValue(Resource.SUCCESS(response.body()!!))
                }
            }catch (e : Exception){
                repoListLiveData.postValue(Resource.ERROR(e.message ?: "Error in Loading Data", null))
            }
        }else{
            val items = itemDatabase.itemDao().getItems()
            val itemRepos = Repos(items)
            repoListLiveData.postValue(Resource.SUCCESS(itemRepos))
        }
    }
}