package com.example.trendingrepos.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trendingrepos.db.ItemDatabase
import com.example.trendingrepos.model.FetchTime
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
                val itemDatabase = ItemDatabase.getDatabase(applicationContext)
                if (response.body() != null){
                    itemDatabase.itemDao().deleteAll()
                    itemDatabase.itemDao().addItems(response.body()!!.items)
                    repoListLiveData.postValue(Resource.SUCCESS(response.body()!!))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        updateFetchTime()
                    }
                }
            }catch (e : Exception){
                repoListLiveData.postValue(Resource.ERROR(e.message ?: "Error in Loading Data", null))
            }
        }else{
            if (timeDifference() >= 7200000){
                itemDatabase.itemDao().deleteAll()
                repoListLiveData.postValue(Resource.ERROR("No Cache available", null))
            }else{
                if (itemDatabase.itemDao().getItems().isEmpty()){
                    repoListLiveData.postValue(Resource.ERROR("No Cache available", null))
                }else {
                    val items = itemDatabase.itemDao().getItems()
                    val itemRepos = Repos(items!!)
                    repoListLiveData.postValue(Resource.SUCCESS(itemRepos))
                }
            }
        }
    }

    private suspend fun updateFetchTime() {
        val fetchTime = FetchTime(id = 0, time = System.currentTimeMillis())
        itemDatabase.fetchTimeDao().addFetchTime(fetchTime)
    }
    private suspend fun timeDifference(): Long {
        val lastFetchTime = itemDatabase.fetchTimeDao().getLastFetchTime()?.time
        val currentTime = System.currentTimeMillis()
        return currentTime - lastFetchTime!!
    }
}