package com.example.foodey.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "getCartItem")
data class Food(
    @PrimaryKey(autoGenerate = false) var id:Int,
    @ColumnInfo(name = "name") var name:String,
    @ColumnInfo(name = "image") var image:String,
    @ColumnInfo(name = "price") var price:String,
    @SerializedName("description")
    @ColumnInfo(name = "desc") var desc:String) : Serializable