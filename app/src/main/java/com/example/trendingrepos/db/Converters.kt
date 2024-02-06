package com.example.trendingrepos.db

import androidx.room.TypeConverter
import com.example.trendingrepos.model.Owner
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromOwner(owner: Owner): String {
        return Gson().toJson(owner)
    }

    @TypeConverter
    fun toOwner(ownerJson: String): Owner {
        return Gson().fromJson(ownerJson, Owner::class.java)
    }
}