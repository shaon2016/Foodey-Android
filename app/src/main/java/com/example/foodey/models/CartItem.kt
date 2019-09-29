package com.example.foodey.models

data class CartItem(
    var id : Int,
    var food : Food,
    var quantity : Int
) {
}