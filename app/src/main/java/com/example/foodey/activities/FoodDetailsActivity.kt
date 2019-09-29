
package com.example.foodey.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidbatch4day7.data.db.AppDb
import com.example.foodey.R
import com.example.foodey.models.CartItem
import com.example.foodey.models.Food
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_food_details.*

class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var f : Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        f = intent?.extras?.getSerializable("food") as Food


        btnAddToCart.setOnClickListener {
            saveIntoCart()
        }
    }

    @SuppressLint("CheckResult")
    private fun saveIntoCart() {
        Observable.fromCallable {
            val ci = CartItem(f.id)
            AppDb.getInstance(this).cartItemDao().insert(ci)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
            },{
                it.printStackTrace()
            })
    }
}
