package com.example.trendingrepos.utils

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observ() : Flow<Status>

    enum class Status{
        Available, Unavailable, Losing, Lost,
    }
}