package com.example.foodey.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidbatch4day7.data.dao.FoodDao
import com.example.foodey.data.dao.CartItemDao
import com.example.foodey.models.Cart
import com.example.foodey.models.CartItem
import com.example.foodey.models.Food

@Database(entities = [Food::class,  CartItem::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun cartItemDao() : CartItemDao

    companion object {
        private var instance: AppDb? = null

        fun getInstance(context: Context) = if (instance == null) create(context) else instance!!

        private fun create(context: Context) = Room.databaseBuilder(
            context,
            AppDb::class.java, "appdb")
            .allowMainThreadQueries()
            .build()
    }
}