package com.example.foodey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodey.models.CartItem

@Dao
interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cartItem: CartItem)

    @Delete
    fun delete(cartItem: CartItem)

    @Query("select * from cart_item")
    fun all() : LiveData<List<CartItem>>

    @Query("select * from cart_item where food_id = :foodId")
    fun getCartItem(foodId:Int) : CartItem?


}