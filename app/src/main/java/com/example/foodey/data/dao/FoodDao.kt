package com.example.androidbatch4day7.data.dao

import androidx.room.*
import com.example.foodey.models.Food

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(f: Food)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(foods: List<Food>)

    @Delete
    fun delete(food: Food)

    @Query("select * from food")
    fun all(): List<Food>

    @Query("select * from food where id = :foodId")
    fun food(foodId: Int): Food
}