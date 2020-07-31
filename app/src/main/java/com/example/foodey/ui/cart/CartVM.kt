package com.example.foodey.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.androidbatch4day7.data.dao.FoodDao
import com.example.foodey.data.dao.CartItemDao
import com.example.foodey.models.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartVM @Inject constructor() : ViewModel() {

    @Inject
     lateinit var cartItemDao: CartItemDao

    @Inject
     lateinit var foodDao: FoodDao

    val items = liveData(Dispatchers.IO) {
        val all = cartItemDao.all1()

        all.forEach { ct ->
            ct.food = foodDao.food(ct.foodId)
        }

        emit(all )
    }

    fun update(ct: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            cartItemDao.insert(ct)
        }
    }
}