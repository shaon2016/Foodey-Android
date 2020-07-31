package com.example.foodey.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.foodey.R
import com.example.foodey.ui.cart.CartActivity
import com.example.foodey.ui.login.LoginActivity
import com.example.foodey.data.P
import com.example.foodey.ui.home.HomeFragment
import com.example.foodey.ui.order.OrderFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.my_toolbar.*

class MainActivity : AppCompatActivity() {

    private var isOrderPosted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // No need to inject this
        // Because we are not injecting anything in this activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNavView()
        configureToolbar()

        isOrderPosted = intent?.extras?.getBoolean("is_order_posted") ?: false
        if (isOrderPosted) initOrderListPage()
        else initHomePage()
    }

    private fun configureToolbar() {

        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

    }


    private fun setNavView() {

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    drawer_layout.closeDrawer(Gravity.LEFT)
                    initHomePage()
                    true
                }
                R.id.nav_order_list -> {
                    drawer_layout.closeDrawer(Gravity.LEFT)
                    initOrderListPage()
                    true
                }
                R.id.nav_logout -> {
                    P.signOut(this)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }


    }

    private fun initOrderListPage() {
        initFragment(OrderFragment.newInstance())
    }

    private fun initHomePage() {
        initFragment(HomeFragment.newInstance())
    }

    private fun initFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
            R.id.main_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
