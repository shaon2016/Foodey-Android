package com.example.foodey.ui.order


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.foodey.R
import com.example.foodey.models.Order
import com.example.foodey.util.obtainViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_order.*
import javax.inject.Inject

class OrderFragment : Fragment() {
    private lateinit var orderAdapter: OrderAdapter

    @Inject lateinit var viewmodelFactory : ViewModelProvider.Factory

    private val  orderVM by lazy {
        obtainViewModel(OrderVM::class.java, viewmodelFactory)

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

        orderAdapter =
            OrderAdapter(context!!, ArrayList())
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
