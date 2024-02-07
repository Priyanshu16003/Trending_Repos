package com.example.trendingrepos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trendingrepos.model.Item

@Dao
interface ItemDao {

    @Insert
    suspend fun addItems(items : List<Item>)

    @Query("SELECT * FROM item")
    suspend fun getItems() : List<Item>

    @Query("DELETE FROM item")
    suspend fun deleteAll()
}