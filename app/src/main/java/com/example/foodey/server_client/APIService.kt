package com.example.foodey.server_client

import com.example.foodey.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @FormUrlEncoded
    @POST("login/views/login.php")
    fun login(
        @Field("mobile") mb : String,
        @Field("password") password : String

              ) : Call<UserSync>


    @GET("data.php")
    fun getFoods() : Call<FoodSync>

    @FormUrlEncoded
    @POST("login/views/signup.php")
    fun signUp(
        @Field("name") name: String,
        @Field("mobile")  mobile: String,
        @Field("password") passW: String) : Call<UserSync>


    @FormUrlEncoded
    @POST("post_order.php")
    fun postOrder(
        @Field("user_id") userId: Int,
        @Field("cart_items_in_ja")  cartItemInJa: String,
        @Field("total_price") totalPrice: Double,
        @Field("discount") discount: Double,
        @Field("vat") vat: Double


    ) : Call<OrderPostSync>

    @GET("orders.php")
    fun getOrders(@Query("user_id") userId:Int) : Call<OrderSync>
}