package com.example.foodey.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodey.R
import com.example.foodey.adapter.FoodRVAdapter
import com.example.foodey.data.P
import com.example.foodey.models.FoodSync
import com.example.foodey.models.User
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNavView()
    }

    private fun setNavView() {

        nav_view.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> {

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

}
