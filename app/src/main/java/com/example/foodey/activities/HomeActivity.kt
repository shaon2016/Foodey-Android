package com.example.foodey.activities

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

    private lateinit var foodAdapter: FoodRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initVar()
        initView()
    }

    private fun initView() {
        rvFoods.layoutManager = GridLayoutManager(this, 2 )
        rvFoods.adapter = foodAdapter

        syncFoods()
    }

    private fun syncFoods() {
        val apiService = RetroClient.getInstance().create(APIService::class.java)

        apiService.getFoods()
            .enqueue(object : Callback<FoodSync> {
                override fun onFailure(call: Call<FoodSync>, t: Throwable) {

                }

                override fun onResponse(call: Call<FoodSync>, response: Response<FoodSync>) {
                    if (response.isSuccessful) {
                        val fs = response.body()

                        fs?.let {
                            Log.d("DATATAG", fs.toString())
                            when (fs.success) {
                                1-> {
                                    foodAdapter.addUniquely(fs.foods)
                                }
                                0-> {

                                }
                                else -> {

                                }
                            }
                        }

                    } else {

                    }
                }
            })


    }

    private fun initVar() {
        foodAdapter = FoodRVAdapter(this, ArrayList())


    }
}
