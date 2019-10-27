package com.example.foodey.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidbatch4day7.data.db.AppDb

import com.example.foodey.R
import com.example.foodey.adapter.FoodRVAdapter
import com.example.foodey.models.Food
import com.example.foodey.models.FoodSync
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var foodAdapter: FoodRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initVar()
        initView()
    }

    private fun initView() {
        rvFoods.layoutManager = GridLayoutManager(context!!, 2)
        rvFoods.adapter = foodAdapter

        syncFoods()
        initSearch()

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
                                1 -> {
                                    foodAdapter.addUniquely(fs.foods)
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

    private fun initSearch() {
        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (foodAdapter.items.isNotEmpty())
                        foodAdapter.filter.filter(s)
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun insertIntoFoodTable(foods: java.util.ArrayList<Food>) {
        Thread {
            AppDb.getInstance(context!!).foodDao().insertAll(foods)
        }.start()
    }

    private fun initVar() {
        foodAdapter = FoodRVAdapter(context!!, ArrayList())


    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
