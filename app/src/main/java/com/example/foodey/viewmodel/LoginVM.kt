package com.example.foodey.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodey.data.P
import com.example.foodey.models.User
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import com.example.foodey.util.U
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginVM (application: Application): AndroidViewModel(application) {

    private val mobile_ = MutableLiveData<String>()
    val mobile: LiveData<String> = mobile_

    private val password_ = MutableLiveData<String>()
    val password: LiveData<String> = password_

    private val isDataLoading_ = MutableLiveData<Boolean>()
    val isDataLoading: LiveData<Boolean> = isDataLoading_

    private val toastMsg_ = MutableLiveData<String>()
    val toastMsg: LiveData<String> = toastMsg_


    fun login() {


        // show progress bar
        isDataLoading_.value = true
        // save data to server

        val apiService = RetroClient.getInstance().create(APIService::class.java)

        apiService.login(mobile_.value!!, password_.value!!)
            .enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {

                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()

                        user?.let { u ->
                            val msg = u.msg

                            when(u.succeess) {
                                0 -> {
                                    toastMsg_.value = msg
                                }

                                1 -> {
                                    u.id = u.succeess
                                    P.saveLoginResponse( getApplication(), u)
                                    toastMsg_.value = msg
                                }
                            }
                        }

                    } else {

                    }

                }
            })

        // in response callback save data to shared pref or local db

        // hide pb

        // redirect to home page


    }

}