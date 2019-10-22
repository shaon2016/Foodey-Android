package com.example.foodey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidbatch4day7.data.db.AppDb
import com.example.foodey.R
import com.example.foodey.models.CartItem


class CheckOutAdapter(
    val context: Context,
    val items: ArrayList<CartItem>,
    private val db: AppDb
) :
    RecyclerView.Adapter<CheckOutAdapter.MyCartVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartVH {
        return MyCartVH(
            LayoutInflater.from(context).inflate(
                R.layout.rv_checkout_foods_row,
                parent,
                false
            )
        )
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


    fun addCartUniquely(newFoods: ArrayList<CartItem>) {
        val oldFoods = items

        for (i in newFoods.indices) {
            var isExits: Boolean? = false

            for (j in oldFoods.indices) {
                if (oldFoods[j].id == newFoods[i].id) {
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


    inner class MyCartVH(v: View) : RecyclerView.ViewHolder(v) {
        private val ivFood = v.findViewById<ImageView>(R.id.ivFood)
        private val ivQuantityIncrement = v.findViewById<ImageView>(R.id.ivQuantityIncrement)
        private val ivQuantityDecrement = v.findViewById<ImageView>(R.id.ivQuantityDecrement)
        private val tvTitle = v.findViewById<TextView>(R.id.tvTitle)
        private val tvPrice = v.findViewById<TextView>(R.id.tvPrice)
        private val tvItemQuantity = v.findViewById<TextView>(R.id.tvItemQuantity)
//        private val tvTotalPrice = v.findViewById<TextView>(R.id.tvTotalPrice)


        fun bind(ct: CartItem) {
            tvTitle.text = ct.food?.name
            tvPrice.text = ct.food?.price
            setQuantityToView(ct.quantity)

            Glide.with(context)
                .load(ct.food?.image)
                .into(ivFood)


            ivQuantityDecrement.setOnClickListener {
                if (ct.quantity > 1) {
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

            itemView.setOnClickListener {


                // Initialize a new instance of
                val builder = AlertDialog.Builder(context)

                // Set the alert dialog title
                builder.setTitle("The Foodey")

                // Display a message on alert dialog
                builder.setMessage("Are you want to delete ${ct.food?.name} Food?")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES") { dialog, which ->
                    // Do something when user press the positive button
                    Toast.makeText(
                        context,
                        "Ok, It's deleted.",
                        Toast.LENGTH_SHORT
                    ).show()


                    val db = AppDb.getInstance(context)
                    db.cartItemDao().delete(ct)



                    items.remove(ct)
                    notifyDataSetChanged()

                }


                // Display a negative button on alert dialog
                builder.setNegativeButton("No") { dialog, which ->
                    Toast.makeText(context, "You are not agree.", Toast.LENGTH_SHORT)
                        .show()
                }


                // Display a neutral button on alert dialog
                builder.setNeutralButton("Cancel") { _, _ ->
                    Toast.makeText(
                        context,
                        "You cancelled the dialog.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()

                // Display the alert dialog on app interface
                dialog.show()
            }
        }

        private fun updateCT(ct: CartItem) {
            Thread {
                db.cartItemDao().insert(ct)
            }.start()
        }


        private fun setQuantityToView(quantity: Int) {
            tvItemQuantity.text = quantity.toString()
        }


    }
}