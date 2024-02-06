package com.example.trendingrepos.repository

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trendingrepos.ConnectivityObserver
import com.example.trendingrepos.NetworkConnectivityObserver
import com.example.trendingrepos.R
import com.example.trendingrepos.db.ItemDatabase
import com.example.trendingrepos.fragments.NoInternetScreenFragment
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.networking.GitHubRepoApi
import com.example.trendingrepos.utils.NetworkUtils

class GitHubProjectRepository(
    private val gitHubRepoApi: GitHubRepoApi,
    private val itemDatabase: ItemDatabase,
    private val applicationContext: Context
) {
    private val repoListLiveData = MutableLiveData<Repos>()
    private val networkConnectivityObserver = NetworkConnectivityObserver(applicationContext)
    val repos : LiveData<Repos>
        get() = repoListLiveData

    suspend fun getTrendingRepos(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            networkConnectivityObserver.observ()
//                .collect(){
//                    when(it){
//                        ConnectivityObserver.Status.Unavailable -> {
//                            Log.d("mytag", "Unavil")
//                        }
//
//                        ConnectivityObserver.Status.Lost -> {Log.d("rep", "Lost")}
//
//                        ConnectivityObserver.Status.Available -> {
//                            Log.d("mytag", "avil")
//                            val response = gitHubRepoApi.getTrendingRepositories()
//                            if (response.body() != null){
//                                itemDatabase.itemDao().deleteAll()
//                                itemDatabase.itemDao().addItems(response.body()!!.items)
//                                repoListLiveData.postValue(response.body())
//                            }
//                        }
//
//                        else -> {
//                            Log.d("mytag", "else")
//                            val items = itemDatabase.itemDao().getItems()
//                            val itemRepos = Repos(items)
//                            repoListLiveData.postValue(itemRepos)
//                        }
//
//                    }
//                }
//        }
        if(NetworkUtils.isInternetAvailable(applicationContext)){
            val response = gitHubRepoApi.getTrendingRepositories()
            if (response.body() != null){
                itemDatabase.itemDao().deleteAll()
                itemDatabase.itemDao().addItems(response.body()!!.items)
                repoListLiveData.postValue(response.body())
            }
        }else{
            val items = itemDatabase.itemDao().getItems()
            val itemRepos = Repos(items)
            repoListLiveData.postValue(itemRepos)
        }
    }
}