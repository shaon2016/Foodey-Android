package com.example.foodey.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodey.models.CartItem

class CartAdapter(val context: Context, val items: ArrayList<CartItem>) : RecyclerView.Adapter<CartAdapter.MyCartVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartVH {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MyCartVH, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class MyCartVH(v: View) : RecyclerView.ViewHolder(v) {

    }
}