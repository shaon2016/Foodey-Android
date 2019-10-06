package com.example.foodey.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodey.data.P
import com.example.foodey.models.UserSync
import com.example.foodey.server_client.APIService
import com.example.foodey.server_client.RetroClient
import com.example.foodey.util.U
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginVM(application: Application) : AndroidViewModel(application) {

    private val mobile_ = MutableLiveData<String>()
    val mobile: LiveData<String> = mobile_

    private val password_ = MutableLiveData<String>()
    val password: LiveData<String> = password_

    private val isDataLoading_ = MutableLiveData<Boolean>()
    val isDataLoading: LiveData<Boolean> = isDataLoading_

    private val toastMsg_ = MutableLiveData<String>()
    val toastMsg: LiveData<String> = toastMsg_

    private val redirectToHomePage_ = MutableLiveData<Boolean>()
    val redirectToHomePage: LiveData<Boolean> = redirectToHomePage_


    fun login() {

        if (isLoginValidate()) {
            // do login
            // show progress bar
            isDataLoading_.value = true
            // save data to server

            val apiService = RetroClient.getInstance().create(APIService::class.java)

            apiService.login(mobile_.value!!, password_.value!!)
                .enqueue(object : Callback<UserSync> {
                    override fun onFailure(call: Call<UserSync>, t: Throwable) {
                        isDataLoading_.value = false
                        toastMsg_.value = "Server error"

                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<UserSync>, response: Response<UserSync>) {
                        isDataLoading_.value = false

                        if (response.isSuccessful) {
                            val userSync = response.body()

                            userSync?.let { u ->
                                val msg = u.msg

                                when (u.success) {
                                    0 -> {
                                        toastMsg_.value = msg
                                    }

                                    1 -> {
                                        val user = userSync.user
                                        P.saveLoginResponse(getApplication(), user)
                                        toastMsg_.value = msg
                                        redirectToHomePage_.value = true
                                    }
                                    else -> {

                                    }
                                }
                            }

                        } else {
                            toastMsg_.value = "Login is not successfull"

                        }
                    }

                })

            /*apiService.login(mobile_.value!!, password_.value!!)
                .enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        isDataLoading_.value = false
                        toastMsg_.value = "Server error"

                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        isDataLoading_.value = false

                        if (response.isSuccessful) {
                            val user = response.body()

                            user?.let { u ->
                                val msg = u.msg

                                when (u.succeess) {
                                    0 -> {
                                        toastMsg_.value = msg
                                    }

                                    1 -> {
                                        u.id = u.succeess
                                        P.saveLoginResponse(getApplication(), u)
                                        toastMsg_.value = msg
                                        redirectToHomePage_.value = true
                                    }
                                    else -> {

                                    }
                                }
                            }

                        } else {
                            toastMsg_.value = "Login is not successfull"

                        }

                    }
                })

*/
        } else {
            toastMsg_.value = "Mobile and password is empty"
        }


    }

    private fun isLoginValidate(): Boolean {
        return !U.isStrEmpty(mobile_.value) && !U.isStrEmpty(password_.value)
    }

    fun setMobileValue(mobileStr: String) {
        mobile_.value = mobileStr
    }

    fun setPasswordValue(passStr: String) {
        password_.value = passStr
    }

}