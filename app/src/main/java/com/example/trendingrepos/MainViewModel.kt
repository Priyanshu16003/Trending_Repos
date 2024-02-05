package com.example.trendingrepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingrepos.model.Contributor
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.networking.GitHubRepoApi
import com.example.trendingrepos.repository.GitHubProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(val repository: GitHubProjectRepository) : ViewModel() {


    init {
        viewModelScope.launch(Dispatchers.IO){
            repository.getTrendingRepos()
        }
    }

    val repos : LiveData<Repos>
        get() = repository.repos

}