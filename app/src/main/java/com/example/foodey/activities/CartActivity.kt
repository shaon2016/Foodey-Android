package com.example.foodey.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidbatch4day7.data.db.AppDb
import com.example.foodey.R
import com.example.foodey.adapter.CartAdapter
import com.example.foodey.models.CartItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {
    private lateinit var db: AppDb
    private lateinit var adapter: CartAdapter
    private lateinit var cartItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initVar()
        initView()
    }

    private fun initView() {

        rvCartItems.layoutManager = LinearLayoutManager(this)
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
                    .subscribe {
                        cartItems = it as ArrayList<CartItem>
                        adapter.refreshWith(it)
                        rvCartItems.adapter = adapter
                    }
            }
        })

        btnCheckOut.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("cart_items", cartItems)
            startActivity(intent)
        }
    }

    private fun initVar() {
        db = AppDb.getInstance(this)
        adapter = CartAdapter(this, ArrayList(), db)

    }

}
