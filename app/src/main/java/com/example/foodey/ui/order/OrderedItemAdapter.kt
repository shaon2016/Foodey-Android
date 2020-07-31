package com.example.foodey.ui.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodey.R
import com.example.foodey.models.OrderedItem

class OrderedItemAdapter(val context: Context, val items : ArrayList<OrderedItem>)
    : RecyclerView.Adapter<OrderedItemAdapter.MyOrderedVH>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderedVH {
        return MyOrderedVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_ordered_item,
                parent, false
            )
        )
    }

    override fun getItemCount() = items.size

    fun getItem(position: Int)  = items[position]

    override fun onBindViewHolder(holder: MyOrderedVH, position: Int) {
        val item = getItem(holder.adapterPosition)
        holder.bind(item)
    }

    class MyOrderedVH(v : View) : RecyclerView.ViewHolder(v) {
        private val tvOrderedItemName = v.findViewById<TextView>(R.id.tvOrderedItemName)

        fun bind(item: OrderedItem) {
            tvOrderedItemName.text = item.name
        }


    }
}