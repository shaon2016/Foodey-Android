package com.example.foodey.ui.order

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodey.data.P
import com.example.foodey.models.Order
import com.example.foodey.models.OrderSync
import com.example.foodey.server_client.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class OrderVM @Inject constructor(val context: Context, private val apiService: APIService) :
    ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun syncOrders() {
        _isLoading.value = true

        apiService.getOrders(P.getUserId(context)).enqueue(object : Callback<OrderSync> {
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