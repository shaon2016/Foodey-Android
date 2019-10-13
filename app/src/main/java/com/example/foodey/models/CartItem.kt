package com.example.foodey.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "cart_item")
data class CartItem(
    @Ignore
    var food: Food?,
    @ColumnInfo(name = "food_id")
    var foodId: Int = 0,
    var quantity: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {


    constructor(foodId: Int, quantity: Int) : this(null, foodId, quantity)
}
