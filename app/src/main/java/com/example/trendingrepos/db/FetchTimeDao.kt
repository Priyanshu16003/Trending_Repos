package com.example.trendingrepos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trendingrepos.model.FetchTime

@Dao
interface FetchTimeDao {

    @Insert
    suspend fun addFetchTime( time : FetchTime)

    @Query("SELECT * FROM `fetch-time` ORDER BY id DESC LIMIT 1")
    suspend fun getLastFetchTime() : FetchTime?
}