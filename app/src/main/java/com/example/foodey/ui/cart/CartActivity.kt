package com.example.foodey.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodey.R
import com.example.foodey.activities.CheckoutActivity
import com.example.foodey.adapter.CartAdapter
import com.example.foodey.data.db.AppDb
import com.example.foodey.models.Cart
import com.example.foodey.models.CartItem
import com.example.foodey.util.SimpleCallback
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cart.*
import javax.inject.Inject

class CartActivity : AppCompatActivity() {
    private lateinit var adapter: CartAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val vm by lazy {
        ViewModelProvider(this, viewModelFactory).get(CartVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initVar()
        initView()
    }

    private fun initView() {

        rvCartItems.layoutManager = LinearLayoutManager(this)

        vm.items.observe(this, Observer {
            it?.let {
                adapter.refreshWith(it as ArrayList<CartItem>)
                rvCartItems.adapter = adapter
            }
        })

        btnCheckOut.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("cart_items", ArrayList<CartItem>(vm.items.value!!))
            startActivity(intent)
        }

        adapter.cartUpdateListener = SimpleCallback {
            vm.update(it)
        }
    }

    private fun initVar() {
        adapter = CartAdapter(this, ArrayList())

    }

}
