package com.example.trendingrepos.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.repository.GitHubProjectRepository
import com.example.trendingrepos.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(val repository: GitHubProjectRepository) : ViewModel() {

    private val _repos = repository.repos
    val repos : LiveData<Resource<Repos>>
        get() = _repos

    init {
        viewModelScope.launch(Dispatchers.IO){
            repository.getTrendingRepos()
        }
    }

}