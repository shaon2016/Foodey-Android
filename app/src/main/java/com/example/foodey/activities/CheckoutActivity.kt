package com.example.foodey.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.annotation.SuppressLint
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidbatch4day7.data.db.AppDb
import com.example.foodey.R
import com.example.foodey.adapter.CheckOutAdapter
import com.example.foodey.models.CartItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.my_toolbar.*

class CheckoutActivity : AppCompatActivity() {

    private lateinit var adapter: CheckOutAdapter
    lateinit var db: AppDb

    lateinit var cart_Items: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
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
        db.cartItemDao().all().observe(this, Observer { cts ->
            cts?.let {
                Observable.fromCallable {
                    cts.forEach { ct ->
                        Thread {
                            ct.food = db.foodDao().food(ct.foodId)

                        }.start()
                    }
                    cts
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { cartitems ->
                        this.cart_Items = cartitems as ArrayList<CartItem>

                        adapter.addCartUniquely(cartitems)
                        rvFoodsCheckout.adapter = adapter
                    }
            }

        })

        var sub_Total = 0.0

        cart_Items.forEach { ct ->
            val price = ct.food?.price?.toDouble() ?: 0.0
            sub_Total = sub_Total + (price * ct.quantity)

        }

        tvSubTotal.text = sub_Total.toString() + "BDT"

        val vat = sub_Total * .15
        tvVat.text = vat.toString() + "BDT"

        val payable = sub_Total + vat
        tvPayable.text = payable.toString() + "BDT"

        val Discount = 100
        tvDiscount.text = "$Discount BDT"


        val DeliveryCharge = 50
        tvDeliveryCharge.text = "$DeliveryCharge BDT"

        val totalAmount = payable - Discount + DeliveryCharge
        tvTotal.text = "$totalAmount + BDT"

        var TotalItemPrice = 0.0
        cart_Items.forEach { cts ->
            val prices = cts.food?.price?.toDouble() ?: 0.0
//            TotalItemPrice = prices * cts.quantity

        }

//        var tvTotalItemPrice2 = findViewById<TextView>(R.id.tvTotalItemPrice)
//        tvTotalItemPrice.text = TotalItemPrice.toString()
//        Log.d("Datatag", tvTotalItemPrice.toString())


    }

    private fun initVar() {

        cart_Items = intent.extras?.getSerializable("cart_items") as ArrayList<CartItem>



        db = AppDb.getInstance(this)
        adapter = CheckOutAdapter(this, ArrayList(), db)


    }

}
