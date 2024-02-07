package com.example.trendingrepos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity("fetch-time")
data class FetchTime(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val time : Long,
)
