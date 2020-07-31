package com.example.foodey.ui.food_details

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodey.R
import com.example.foodey.data.db.AppDb
import com.example.foodey.models.CartItem
import com.example.foodey.models.Food
import com.example.foodey.ui.cart.CartActivity
import com.example.foodey.util.obtainViewModel
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_food_details.*
import javax.inject.Inject

class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var f: Food

    @Inject lateinit var viewmodelFactory : ViewModelProvider.Factory

    private val vm by lazy { obtainViewModel(DetailsVM::class.java, viewmodelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        f = intent?.extras?.getSerializable("getCartItem") as Food

        vm.setFood(f)


        btnAddToCart.setOnClickListener {
            vm.addToCart()
        }

        tvTitleDetails.text = f.name
        tvPriceDetails.text = "${f.price} BDT"
        tvDescDetails.text = "${f.desc}"
        Glide.with(this)
            .load(f.image)
            .into(ivFoodDetails)

        vm.toast.observe(this, Observer {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
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
