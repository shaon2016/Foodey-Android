package com.example.foodey.models

import com.google.gson.annotations.SerializedName

data class Orders(
    @SerializedName("id")
    var orderId: Int,
    @SerializedName("user_id")
    var userId:Int,
    @SerializedName("items")
    var items : ArrayList<OrderedItem>,
    @SerializedName("payable_price")
    var payablePrice : Double,
    var discount : Double,
    var vat :Double,
    @SerializedName("created_at")
    var createdAt :String
    )