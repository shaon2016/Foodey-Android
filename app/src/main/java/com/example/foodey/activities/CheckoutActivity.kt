package com.example.foodey.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.androidbatch4day7.data.db.AppDb
import com.example.foodey.R
import com.example.foodey.adapter.CheckOutAdapter
import com.example.foodey.data.P
import com.example.foodey.models.CartItem
import com.example.foodey.models.OrderPostSync
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import com.example.foodey.ui.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.my_toolbar.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private lateinit var adapter: CheckOutAdapter
    lateinit var db: AppDb

    lateinit var cart_Items: ArrayList<CartItem>
    private var totalPrice = 0.0
    private var discount = 0.0
    private var vat = 0.0

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

        vat = sub_Total * .15
        tvVat.text = vat.toString() + "BDT"

        val payable = sub_Total + vat
        tvPayable.text = payable.toString() + "BDT"

        discount = 100.0
        tvDiscount.text = "$discount BDT"


        val DeliveryCharge = 50
        tvDeliveryCharge.text = "$DeliveryCharge BDT"

        totalPrice = payable - discount + DeliveryCharge
        tvTotal.text = "$totalPrice + BDT"

        var TotalItemPrice = 0.0
        cart_Items.forEach { cts ->
            val prices = cts.food?.price?.toDouble() ?: 0.0
//            TotalItemPrice = prices * cts.quantity

        }


        btnPostOrder.setOnClickListener {
            postOrder()
        }
    }


    private fun initVar() {

        cart_Items = intent.extras?.getSerializable("cart_items") as ArrayList<CartItem>



        db = AppDb.getInstance(this)
        adapter = CheckOutAdapter(this, ArrayList(), db)


    }

    private fun postOrder() {
        MaterialDialog(this).show {
            positiveButton {
                confirmOrder()
            }
            negativeButton {
                dismiss()
            }
        }
    }

    private fun confirmOrder() {
        val apiService = RetroClient.getInstance().create(APIService::class.java)


        val call = apiService.postOrder(
            P.getUserId(this),
            getCartItemsInJaString(),
            totalPrice,
            discount,
            vat
        )

        call.enqueue(object : Callback<OrderPostSync> {
            override fun onFailure(call: Call<OrderPostSync>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<OrderPostSync>, response: Response<OrderPostSync>) {
                if (response.isSuccessful) {
                    val ops = response.body()
                    if (ops != null && ops.success == 1) {
                        // Order successful
                        deleteCartData()

                        // TODO GO to order fragment
                        val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
                        intent.putExtra("is_order_posted", true)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)

                    } else {
                        // TODO Order successfully not posted

                    }

                } else {
                    // TODO Order successfully not posted
                }
            }

        })


    }

    private fun getCartItemsInJaString(): String {
        val ja = JSONArray()
        cart_Items.forEach { ct ->
            val jo = JSONObject()

            val food = AppDb.getInstance(this).foodDao().food(ct.foodId)
            jo.put("id", food.id)
            jo.put("name", food.name)
            jo.put("price", food.price)
            jo.put("quantity", ct.quantity)

            ja.put(jo)
        }

        return ja.toString()
    }

    private fun deleteCartData() {
        Thread {
            AppDb.getInstance(this).cartItemDao().deleteAll()
        }.start()
    }


}
