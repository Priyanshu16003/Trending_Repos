package com.example.trendingrepos.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trendingrepos.db.ItemDatabase
import com.example.trendingrepos.model.Contributor
import com.example.trendingrepos.model.FetchTime
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.networking.GitHubRepoApi
import com.example.trendingrepos.utils.NetworkUtils
import com.example.trendingrepos.utils.Resource
import java.util.Calendar

class GitHubProjectRepository(
    private val gitHubRepoApi: GitHubRepoApi,
    private val itemDatabase: ItemDatabase,
    private val applicationContext: Context
) {
    private val repoListLiveData = MutableLiveData<Resource<Repos>>()
    private val contributorAvatarLivedata = MutableLiveData<Resource<List<Contributor>>>()
    val repos : LiveData<Resource<Repos>>
        get() = repoListLiveData

    val contributorAvatar : LiveData<Resource<List<Contributor>>>
        get() = contributorAvatarLivedata
    suspend fun getTrendingRepos(){
        repoListLiveData.postValue(Resource.LOADING(null))
        if(NetworkUtils.isInternetAvailable(applicationContext)){
            fetchFromNetwork()
        }else{
            fetchFromDatabase()
        }
    }

    private suspend fun fetchFromDatabase() {
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

    private suspend fun fetchFromNetwork() {
        try {
            val response = gitHubRepoApi.getTrendingRepositories()
            if (response.body() != null){
                itemDatabase.itemDao().deleteAll()
                itemDatabase.itemDao().addItems(response.body()!!.items)
                repoListLiveData.postValue(Resource.SUCCESS(response.body()!!))
                updateFetchTime()

            }
        }catch (e : Exception){
            repoListLiveData.postValue(Resource.ERROR(e.message ?: "Error in Loading Data", null))
        }
    }

    suspend fun getContributorAvatar(login : String , name : String){
        contributorAvatarLivedata.postValue(Resource.LOADING(null))
        if (NetworkUtils.isInternetAvailable(applicationContext)){
            try {
                val response = gitHubRepoApi.getContributorsAvatar(name, login)
                if (response.body() != null){
                    contributorAvatarLivedata.postValue(Resource.SUCCESS(response.body()!!))
                }
            }catch (e : Exception){
                contributorAvatarLivedata.postValue(Resource.ERROR(e.message ?: "Error in Loading data", null))
            }
        }
    }

    private suspend fun updateFetchTime() {
        val fetchTime = FetchTime(id = 0, time = Calendar.getInstance().timeInMillis)
        itemDatabase.fetchTimeDao().addFetchTime(fetchTime)
    }
    private suspend fun timeDifference(): Long {
        val lastFetchTime = itemDatabase.fetchTimeDao().getLastFetchTime()?.time
        val currentTime = System.currentTimeMillis()
        return currentTime - lastFetchTime!!
    }
}