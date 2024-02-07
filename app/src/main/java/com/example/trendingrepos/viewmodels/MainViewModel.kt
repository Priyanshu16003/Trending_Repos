package com.example.trendingrepos.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.repository.GitHubProjectRepository
import com.example.trendingrepos.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(private val repository: GitHubProjectRepository) : ViewModel() {

    private val _repos = repository.repos
    val repos : LiveData<Resource<Repos>>
        get() = _repos

    fun fetchTrendingRepos(){
        viewModelScope.launch(Dispatchers.IO){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                repository.getTrendingRepos()
            }
        }
    }



}