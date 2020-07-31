package com.example.foodey.ui.checkout

import android.content.Context
import androidx.lifecycle.*
import com.example.androidbatch4day7.data.dao.FoodDao
import com.example.foodey.data.P
import com.example.foodey.data.dao.CartItemDao
import com.example.foodey.models.CartItem
import com.example.foodey.models.Food
import com.example.foodey.models.OrderPostSync
import com.example.foodey.server_client.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CheckoutVM @Inject constructor(
    private val context: Context,
    private val apiService: APIService
) : ViewModel() {

    @Inject
    lateinit var cartItemDao: CartItemDao

    @Inject
    lateinit var foodDao: FoodDao

    private val _orderPosted = MutableLiveData<Boolean>()
    val orderPosted: LiveData<Boolean> get() = _orderPosted

    val cartItems = MutableLiveData<List<CartItem>>()

    fun confirmOrder(totalPrice: Double, vat: Double) {

        val call = apiService.postOrder(
            P.getUserId(context),
            getCartItemsInJaString(),
            totalPrice,
            100.0,
            vat
        )

        call.enqueue(object : Callback<OrderPostSync> {
            override fun onFailure(call: Call<OrderPostSync>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<OrderPostSync>, response: Response<OrderPostSync>) {
                if (response.isSuccessful) {
                    val ops = response.body()
                    if (ops != null && ops.success == 1) {
                        // Order successful
                        deleteCartData()

                        _orderPosted.value = true


                    } else {
                        // TODO Order successfully not posted
                        _orderPosted.value = false
                    }

                } else {
                    // TODO Order successfully not posted
                    _orderPosted.value = false
                }
            }

        })


    }

    private fun getCartItemsInJaString(): String {
        val ja = JSONArray()
        cartItems.value?.forEach { ct ->
            val jo = JSONObject()

            val food = foodDao.food(ct.foodId)
            jo.put("id", food.id)
            jo.put("name", food.name)
            jo.put("price", food.price)
            jo.put("quantity", ct.quantity)

            ja.put(jo)
        }

        return ja.toString()
    }

    private fun deleteCartData() {
        CoroutineScope(Dispatchers.IO).launch {
            cartItemDao.deleteAll()
        }
    }

    fun update(ct: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            cartItemDao.insert(ct)
        }

        getCartItems()
    }

     fun getCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = cartItemDao.all1()
            items.forEach { ct ->
                ct.food = foodDao.food(ct.foodId)
            }

            cartItems.postValue(items)
        }
    }


}