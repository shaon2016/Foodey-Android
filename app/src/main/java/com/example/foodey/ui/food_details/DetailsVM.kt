package com.example.foodey.ui.food_details

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodey.data.dao.CartItemDao
import com.example.foodey.models.CartItem
import com.example.foodey.models.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsVM @Inject constructor(private val cartItemDao: CartItemDao) : ViewModel() {

    val food = MutableLiveData<Food>()

    val toast = MutableLiveData<String>()

    fun addToCart() {
        // Checking getCartItem already exists or not
        viewModelScope.launch(Dispatchers.IO) {
            var cartItem = cartItemDao.getCartItem(food.value!!.id)

            if (cartItem != null && cartItem.id > 0) {
                cartItem.quantity++
            } else {
                cartItem = CartItem(food.value!!.id, 1)
            }
            cartItemDao.insert(cartItem)

            toast.postValue("Added to cart")
        }
    }

    fun setFood(f: Food) {
        food.value = f
    }
}