package com.example.trendingrepos

import android.app.Application
import android.util.Log
import com.example.trendingrepos.db.ItemDatabase
import com.example.trendingrepos.networking.RetrofitInstance
import com.example.trendingrepos.repository.GitHubProjectRepository

class TrendingRepoApplication : Application() {

    lateinit var gitHubProjectRepository: GitHubProjectRepository
    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val gitHubRepoApi = RetrofitInstance.trendingRepoApi
        val database = ItemDatabase.getDatabase(applicationContext)
        gitHubProjectRepository = GitHubProjectRepository(gitHubRepoApi, database, applicationContext)
    }
}