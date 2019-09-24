package com.example.foodey.server_client

import com.example.foodey.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIService {

    @FormUrlEncoded
    @POST("login/views/login.php")
    fun login(
        @Field("mobile") mb : String,
        @Field("password") password : String

              ) : Call<User>
}