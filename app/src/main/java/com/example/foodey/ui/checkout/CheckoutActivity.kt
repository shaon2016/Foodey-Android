package com.example.foodey.ui.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.foodey.R
import com.example.foodey.models.CartItem
import com.example.foodey.ui.MainActivity
import com.example.foodey.util.SimpleCallback
import com.example.foodey.util.obtainViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.my_toolbar.*
import javax.inject.Inject

class CheckoutActivity : AppCompatActivity() {

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory
    private val vm by lazy {
        obtainViewModel(CheckoutVM::class.java, viewmodelFactory)
    }

    private lateinit var adapter: CheckOutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        initVar()
        configureToolbar()
        initView()
    }


    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = getString(R.string.CheckOut)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar?.setNavigationOnClickListener { v -> onBackPressed() }
    }


    @SuppressLint("WrongConstant", "SetTextI18n")
    private fun initView() {
        rvFoodsCheckout.layoutManager =
            LinearLayoutManager(this, LinearLayoutCompat.HORIZONTAL, false)

        vm.getCartItems()

        vm.cartItems.observe(this, Observer { cartitems ->
            cartitems?.let {
                cartitems as ArrayList<CartItem>
                adapter.addCartUniquely(cartitems)
                rvFoodsCheckout.adapter = adapter

                updateView(cartitems)
            }
        })

        vm.orderPosted.observe(this, Observer {
            it?.let {
                if (it) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("is_order_posted", true)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to order", Toast.LENGTH_LONG).show()
                }
            }
        })

        btnPostOrder.setOnClickListener {
            postOrder()
        }

        adapter.cartUpdateListener = SimpleCallback {
            vm.update(it)
        }
        adapter.cartDeleteListener = SimpleCallback {
            vm.delete(it)
        }
    }

    private fun updateView(cartitems: java.util.ArrayList<CartItem>) {
        var sub_Total = 0.0
        cartitems.forEach { ct ->
            val price = ct.food?.price?.toDouble() ?: 0.0
            sub_Total += (price * ct.quantity)
        }
        tvSubTotal.text = sub_Total.toString() + "BDT"


        val vat = sub_Total * .15
        tvVat.text = vat.toString() + "BDT"

        val payable = sub_Total + vat
        tvPayable.text = payable.toString() + "BDT"

        val discount = 100
        tvDiscount.text = "$discount BDT"

        val DeliveryCharge = 50
        tvDeliveryCharge.text = "$DeliveryCharge BDT"

        val totalPrice = payable - discount + DeliveryCharge
        tvTotal.text = "$totalPrice + BDT"
    }


    private fun initVar() {
        adapter =
            CheckOutAdapter(this, ArrayList())
    }

    private fun postOrder() {
        MaterialDialog(this).show {
            positiveButton {
                vm.confirmOrder(0.0, 0.0)
            }
            negativeButton {
                dismiss()
            }
        }
    }


}
