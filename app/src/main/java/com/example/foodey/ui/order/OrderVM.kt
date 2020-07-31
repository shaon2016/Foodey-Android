package com.example.foodey.ui.order

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodey.data.P
import com.example.foodey.models.Order
import com.example.foodey.models.OrderSync
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class OrderVM @Inject constructor(val context: Context) : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun syncOrders() {
        _isLoading.value = true

        val service = RetroClient.getInstance().create(APIService::class.java)
        val call = service.getOrders(P.getUserId(context))
        call.enqueue(object : Callback<OrderSync> {
            override fun onFailure(call: Call<OrderSync>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<OrderSync>, response: Response<OrderSync>) {
                if (response.isSuccessful) {
                    val os = response.body()

                    os?.let {

                        _orders.value = os.orders

                    }
                } else {

                }
            }
        })
        _isLoading.value = false
    }

}