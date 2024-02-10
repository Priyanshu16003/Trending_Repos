package com.example.trendingrepos.viewmodels

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingrepos.model.Contributor
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.repository.GitHubProjectRepository
import com.example.trendingrepos.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(private val repository: GitHubProjectRepository) : ViewModel() {

    private val _repos = repository.repos
    private val _contributorAvatar =repository.contributorAvatar
    val repos : LiveData<Resource<Repos>> get() = _repos
    val contributorAvtar : LiveData<Resource<List<Contributor>>> get() = _contributorAvatar

    fun fetchTrendingRepos(){
        viewModelScope.launch(Dispatchers.Main){
                repository.getTrendingRepos()
        }
    }

    fun fetchContributorsAvtar(login : String, name : String){
        viewModelScope.launch(Dispatchers.Main) {
            repository.getContributorAvatar(login,name)
        }
    }
}