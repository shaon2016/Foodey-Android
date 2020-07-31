package com.example.foodey.ui.order


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.foodey.R
import com.example.foodey.adapter.OrderAdapter
import com.example.foodey.data.P
import com.example.foodey.di.annotation.ViewModelKey
import com.example.foodey.models.Order
import com.example.foodey.models.OrderSync
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import com.example.foodey.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class OrderFragment : Fragment() {
    private lateinit var orderAdapter: OrderAdapter

    @Inject lateinit var viewmodelFactory : ViewModelProvider.Factory

    private val  orderVM by lazy {
        ViewModelProvider(requireActivity(), viewmodelFactory).get(OrderVM::class.java)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

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

        orderVM.orders.observe(requireActivity(), Observer {
            it?.let {
                orderAdapter.addCartUniquely(it as ArrayList<Order>)
            }
        })
        orderVM.isLoading.observe(requireActivity(), Observer {
            it?.let {
                pbOrder.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        orderVM.syncOrders()
    }


    companion object {
        @JvmStatic
        fun newInstance() = OrderFragment()

    }
}
