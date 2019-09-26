package com.example.foodey.models

import com.google.gson.annotations.SerializedName

data class FoodSync(
    @SerializedName("success")
    var success: Int,
    @SerializedName("data")
    var foods: ArrayList<Food>)