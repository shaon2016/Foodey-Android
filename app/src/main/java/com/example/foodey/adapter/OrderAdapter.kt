package com.example.foodey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodey.R
import com.example.foodey.models.Order
import com.example.foodey.models.OrderedItem

class OrderAdapter(val context: Context, val items : ArrayList<Order>)
    : RecyclerView.Adapter<OrderAdapter.MyOrderVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderVH {
        return MyOrderVH(LayoutInflater.from(parent.context).inflate(R.layout.rv_order_row, parent, false))
    }

    override fun getItemCount() = items.size

    private fun getItem(pos : Int) = items[pos]

    override fun onBindViewHolder(holder: MyOrderVH, position: Int) {
        val item = getItem(holder.adapterPosition)
        holder.bind(item)
    }

    fun addCartUniquely(newFoods: ArrayList<Order>) {
        val oldFoods = items

        for (i in newFoods.indices) {
            var isExits: Boolean? = false

            for (j in oldFoods.indices) {
                if (oldFoods[j].orderId == newFoods[i].orderId) {
                    isExits = true
                    break
                }
            }
            if (!isExits!!) {
                items.add(newFoods[i])

            }
        }
        notifyDataSetChanged()
    }



    inner class MyOrderVH(v : View) : RecyclerView.ViewHolder(v) {
        private val tvTotal = v.findViewById<TextView>(R.id.tvTotal)
        private val tvDiscount = v.findViewById<TextView>(R.id.tvDiscount)
        private val rvOrderedFoods = v.findViewById<RecyclerView>(R.id.rvOrderedFoods)

        fun bind(item: Order) {
            tvTotal.text = "${item.payablePrice} BDT"
            tvDiscount.text = "${item.discount} BDT"

            showOrderedItems(item.itemsList)
        }

        private fun showOrderedItems(orderedItems: java.util.ArrayList<OrderedItem>) {
            rvOrderedFoods.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            val adapter = OrderedItemAdapter(context, orderedItems)
            rvOrderedFoods.adapter = adapter
        }

    }
}