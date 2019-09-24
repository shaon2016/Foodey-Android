package com.example.foodey.models

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("success")
    var succeess : Int,
    @SerializedName("message")
    var msg : String,
    var id : Int ,
    var name : String,
    var mobile : String
)
