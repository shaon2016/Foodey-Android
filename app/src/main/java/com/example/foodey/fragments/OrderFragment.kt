package com.example.foodey.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.foodey.R
import com.example.foodey.data.P
import com.example.foodey.models.OrderSync
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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

                    os?.let {
                        val orders = os.orders

                        // adapter.addAll(orders)


                        //
                    }
                }else {

                }
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() = OrderFragment()

    }
}
