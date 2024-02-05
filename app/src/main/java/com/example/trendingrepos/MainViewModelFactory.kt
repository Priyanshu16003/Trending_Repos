package com.example.trendingrepos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trendingrepos.networking.GitHubRepoApi
import com.example.trendingrepos.repository.GitHubProjectRepository

class MainViewModelFactory(val repository: GitHubProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}