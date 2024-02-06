package com.example.trendingrepos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("owner")
data class Owner(
    @PrimaryKey(autoGenerate = true)
    val ownerId : Int,
    val login : String
)