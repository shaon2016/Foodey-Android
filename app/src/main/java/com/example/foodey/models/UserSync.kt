package com.example.foodey.models

import com.google.gson.annotations.SerializedName

data class UserSync(
    var success: Int,
    @SerializedName("message")
    var msg: String,
    @SerializedName("data")
    var user: User
    )