package com.example.foodey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidbatch4day7.data.db.AppDb
import com.example.foodey.R
import com.example.foodey.models.CartItem


class CartAdapter(
    val context: Context,
    val items: ArrayList<CartItem>,
    private val db: AppDb
) :
    RecyclerView.Adapter<CartAdapter.MyCartVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartVH {
        return MyCartVH(LayoutInflater.from(context).inflate(R.layout.rv_cart_row, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MyCartVH, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): CartItem {
        return items[position]
    }

    private fun addAll(cartItems: List<CartItem>) {
        items.addAll(cartItems)
        notifyDataSetChanged()
    }

    fun addUniquely(newItems: List<CartItem>) {

    }


    fun refreshWith(items: List<CartItem>) {
        this.items.clear()
        addAll(items)
    }

    inner class MyCartVH(v: View) : RecyclerView.ViewHolder(v) {
        private val ivFood = v.findViewById<ImageView>(R.id.ivFood)
        private val ivQuantityIncrement = v.findViewById<ImageView>(R.id.ivQuantityIncrement)
        private val ivQuantityDecrement = v.findViewById<ImageView>(R.id.ivQuantityDecrement)
        private val tvTitle = v.findViewById<TextView>(R.id.tvTitle)
        private val tvPrice = v.findViewById<TextView>(R.id.tvPrice)
        private val tvQuantity = v.findViewById<TextView>(R.id.tvQuantity)

        fun bind(ct: CartItem) {
            tvTitle.text = ct.food?.name
            tvPrice.text = ct.food?.price
            setQuantityToView(ct.quantity)

            Glide.with(context)
                .load(ct.food?.image)
                .into(ivFood)

            ivQuantityDecrement.setOnClickListener {
                if (ct.quantity >  1) {
                    ct.quantity--
                    setQuantityToView(ct.quantity)
                    updateCT(ct)
                }
            }

            ivQuantityIncrement.setOnClickListener {
                ct.quantity++
                setQuantityToView(ct.quantity)
                updateCT(ct)
            }
        }

        private fun updateCT(ct: CartItem) {
            Thread {
                db.cartItemDao().insert(ct)
            }.start()
        }

        private fun setQuantityToView(quantity: Int) {
            tvQuantity.text = quantity.toString()
        }
    }
}