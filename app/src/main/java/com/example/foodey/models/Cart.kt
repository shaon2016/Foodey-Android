package com.example.foodey.models

data class Cart(
    var id:Int,
    var userId:Int,
    var cartItems : ArrayList<CartItem>)
