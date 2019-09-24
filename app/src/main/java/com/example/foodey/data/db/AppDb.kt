package com.example.androidbatch4day7.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidbatch4day7.data.dao.FoodDao
import com.example.foodey.models.Food

@Database(entities = [Food::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        private var instance: AppDb? = null

        fun getInstance(context: Context) =  if (instance == null) create(context) else  instance!!

        private fun create(context: Context) = Room.databaseBuilder(context,
            AppDb::class.java, "food")
            .allowMainThreadQueries()
            .build()
    }
}