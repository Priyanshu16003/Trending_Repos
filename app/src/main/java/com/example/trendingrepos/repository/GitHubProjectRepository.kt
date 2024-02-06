package com.example.trendingrepos.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trendingrepos.db.ItemDatabase
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.networking.GitHubRepoApi
import com.example.trendingrepos.utils.NetworkUtils

class GitHubProjectRepository(
    private val gitHubRepoApi: GitHubRepoApi,
    private val itemDatabase: ItemDatabase,
    private val applicationContext: Context
) {
    private val repoListLiveData = MutableLiveData<Repos>()
    val repos : LiveData<Repos>
        get() = repoListLiveData

    suspend fun getTrendingRepos(){

        if(NetworkUtils.isInternetAvailable(applicationContext)){
            val response = gitHubRepoApi.getTrendingRepositories("watchers")
            if (response.body() != null){
                itemDatabase.itemDao().deleteAll()
                itemDatabase.itemDao().addItems(response.body()!!.items)
                repoListLiveData.postValue(response.body())
            }
        }else{
            val items = itemDatabase.itemDao().getItems()
            Log.d("qwe","Loading from db")
            val itemRepos = Repos(items)
            repoListLiveData.postValue(itemRepos)
        }


    }
}