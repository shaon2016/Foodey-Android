package com.example.foodey.ui.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidbatch4day7.data.db.AppDb

import com.example.foodey.R
import com.example.foodey.adapter.FoodRVAdapter
import com.example.foodey.models.Food
import com.example.foodey.models.FoodSync
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeFragment : Fragment() {
    private lateinit var foodAdapter: FoodRVAdapter

    private lateinit var homeVM: HomeVM

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

        homeVM.syncFoods()
        initSearch()

        homeVM.foods.observe(requireActivity(), Observer {
            it?.let {
                foodAdapter.addUniquely(it as java.util.ArrayList<Food>)
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


    private fun initVar() {
        homeVM = ViewModelProvider(requireActivity()).get(HomeVM::class.java)
        foodAdapter = FoodRVAdapter(context!!, ArrayList())
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
