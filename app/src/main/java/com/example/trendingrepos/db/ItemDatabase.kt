package com.example.trendingrepos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trendingrepos.model.Item
import com.example.trendingrepos.model.Owner

@Database(entities = [Item::class, Owner::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ItemDatabase : RoomDatabase() {

    abstract fun itemDao() : ItemDao

    companion object{
        @Volatile
        private var INSTANCE : ItemDatabase? = null

        fun getDatabase(context: Context) : ItemDatabase{
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,
                        ItemDatabase::class.java,
                        "itemDb",)
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}