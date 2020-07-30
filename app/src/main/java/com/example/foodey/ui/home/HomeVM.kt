package com.example.foodey.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidbatch4day7.data.db.AppDb
import com.example.foodey.models.Food
import com.example.foodey.models.FoodSync
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class HomeVM(application: Application) : AndroidViewModel(application) {
    private val db = AppDb.getInstance(application.applicationContext)!!

    private val _foods = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>> get() = _foods

    fun insertIntoFoodTable(foods: ArrayList<Food>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.foodDao().insertAll(foods)
        }
    }

     fun syncFoods() {
        val apiService = RetroClient.getInstance().create(APIService::class.java)

        apiService.getFoods()
            .enqueue(object : Callback<FoodSync> {
                override fun onFailure(call: Call<FoodSync>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<FoodSync>, response: Response<FoodSync>) {
                    if (response.isSuccessful) {
                        val fs = response.body()

                        fs?.let {

                            when (fs.success) {
                                1 -> {
                                    _foods.value = fs.foods
                                    insertIntoFoodTable(fs.foods)
                                }
                                0 -> {

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

}