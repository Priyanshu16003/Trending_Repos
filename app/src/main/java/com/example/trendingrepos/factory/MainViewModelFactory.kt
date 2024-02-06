package com.example.trendingrepos.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trendingrepos.repository.GitHubProjectRepository
import com.example.trendingrepos.viewmodels.MainViewModel

class MainViewModelFactory(val repository: GitHubProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}