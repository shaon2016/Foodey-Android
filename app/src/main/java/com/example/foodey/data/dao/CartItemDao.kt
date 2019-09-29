package com.example.foodey.data.dao

import androidx.room.*
import com.example.foodey.models.CartItem

@Dao
interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cartItem: CartItem)

    @Delete
    fun delete()




}