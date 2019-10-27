package com.example.foodey.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.foodey.R
import com.example.foodey.adapter.OrderAdapter
import com.example.foodey.data.P
import com.example.foodey.models.OrderSync
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import kotlinx.android.synthetic.main.fragment_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class OrderFragment : Fragment() {
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        orderAdapter = OrderAdapter(context!!, ArrayList())
        rvOrders.layoutManager = LinearLayoutManager(context)
        rvOrders.adapter = orderAdapter
        syncOrders()
    }

    private fun syncOrders() {
        // TODO progress bar // Loader

        val service = RetroClient.getInstance().create(APIService::class.java)
        val call = service.getOrders(P.getUserId(context!!))
        call.enqueue(object : Callback<OrderSync> {
            override fun onFailure(call: Call<OrderSync>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<OrderSync>, response: Response<OrderSync>) {
                if (response.isSuccessful) {
                    val os = response.body()
                    Log.d("DATATAG", os.toString())

                    os?.let {
                        val orders = os.orders

                        orderAdapter.addCartUniquely(orders)

                    }
                } else {

                }
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() = OrderFragment()

    }
}
