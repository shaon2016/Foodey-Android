package com.example.foodey.server_client

import com.example.foodey.util.C
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroClient {

    private var INSTANCE: Retrofit? = null

    fun getInstance(): Retrofit {
        return if (INSTANCE == null) {
            create()
        } else INSTANCE!!
    }

    private fun create(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(C.BASE_URL)
            .build()
    }
}