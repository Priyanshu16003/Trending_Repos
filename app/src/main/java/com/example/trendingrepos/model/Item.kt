package com.example.trendingrepos.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val itemId : Int,
    val collaborators_url: String,
    val description: String,
    val forks_count: Int,
    val full_name: String,
    val id: Int,
    val language: String?,
    val name: String,
    @Embedded
    val owner: Owner,
    val score: Double,
    val stargazers_count: Int,
)