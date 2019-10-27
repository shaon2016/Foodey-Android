package com.example.foodey.models

import android.util.Log
import com.google.gson.annotations.SerializedName
import org.json.JSONArray

data class Order(
    @SerializedName("id")
    var orderId: Int,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("items")
    var itemsStringJa: String,
    var itemsList: ArrayList<OrderedItem>,
    @SerializedName("payable_price")
    var payablePrice: Double,
    var discount: Double,
    var vat: Double,
    @SerializedName("created_at")
    var createdAt: String
) {

    fun getOrderedItemList(): ArrayList<OrderedItem> {
        if (itemsStringJa.isNotEmpty()) {
            val ja = JSONArray(itemsStringJa)

            itemsList = OrderedItem.parseOrderedItems(ja)

            Log.d("items_ja", ja.toString())

            return itemsList
        } else return ArrayList()
    }


}