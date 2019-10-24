package com.example.foodey.models

import org.json.JSONArray
import org.json.JSONObject

data class OrderedItem(
    var id: Int,
    var name: String,
    var price: Double,
    var quantity: Int
) {

    companion object {
        fun parseOrderedItems(ja: JSONArray): ArrayList<OrderedItem> {
            val l = ja.length()
            val items = ArrayList<OrderedItem>()

            (0 until l).mapTo(items) { pos ->
                parseOrderedItem(ja.getJSONObject(pos))
            }

            return items
        }

        private fun parseOrderedItem(jo: JSONObject): OrderedItem {
            return OrderedItem(
                jo.getInt("id"),
                jo.getString("name"),
                jo.getDouble("price"),
                jo.getInt("quantity")
            )
        }
    }
}