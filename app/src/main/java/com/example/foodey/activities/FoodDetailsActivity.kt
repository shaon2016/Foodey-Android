package com.example.foodey.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.androidbatch4day7.data.db.AppDb
import com.example.foodey.R
import com.example.foodey.models.CartItem
import com.example.foodey.models.Food
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_food_details.*

class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var f: Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        f = intent?.extras?.getSerializable("getCartItem") as Food


        btnAddToCart.setOnClickListener {
            saveIntoCart()
        }

        tvTitleDetails.text = f.name
        tvPriceDetails.text = "${f.price} BDT"
        tvDescDetails.text = "${f.desc}"
        Glide.with(this)
            .load(f.image)
            .into(ivFoodDetails)

    }

    @SuppressLint("CheckResult")
    private fun saveIntoCart() {
        Observable.fromCallable {
            val cartItemDao = AppDb.getInstance(this).cartItemDao()
            // Checking getCartItem already exists or not
            var cartItem = cartItemDao.getCartItem(f.id)

            if (cartItem != null && cartItem.id > 0) {
                cartItem.quantity++
            } else {
                cartItem = CartItem(f.id, 1)
            }
            cartItemDao.insert(cartItem)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
            }, {
                it.printStackTrace()
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
