package com.example.androidbatch4day7.data.dao

import androidx.room.*
import com.example.foodey.models.Food

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(f: Food)

    @Delete
    fun delete(food: Food)

    @Query("select * from food")
    fun all() : List<Food>
}