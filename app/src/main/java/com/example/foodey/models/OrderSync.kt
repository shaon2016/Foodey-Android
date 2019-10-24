package com.example.foodey.models

import androidx.room.FtsOptions
import com.google.gson.annotations.SerializedName

data class OrderSync(
    var success: Int = 0,
    @SerializedName("data")
    var orders: ArrayList<Orders>


)