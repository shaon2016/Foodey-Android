package com.example.foodey.ui.signup

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodey.data.P
import com.example.foodey.models.UserSync
import com.example.foodey.server_client.APIService
import com.example.foodey.util.U
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SignUpVM @Inject constructor(val application: Application, val apiService: APIService) : ViewModel() {

    private val name_ = MutableLiveData<String>()

    private val mobile_ = MutableLiveData<String>()

    private val password_ = MutableLiveData<String>()

    private val isDataLoading_ = MutableLiveData<Boolean>()
    val isDataLoading: LiveData<Boolean> = isDataLoading_

    private val toastMsg_ = MutableLiveData<String>()
    val toastMsg: LiveData<String> = toastMsg_

    private val redirectToHomePage_ = MutableLiveData<Boolean>()
    val redirectToHomePage: LiveData<Boolean> = redirectToHomePage_


    fun register() {
        if (isLoginValidate()) {
            // do login
            // show progress bar
            isDataLoading_.value = true
            // save data to server

            apiService.signUp(name_.value!!, mobile_.value!!, password_.value!!)
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
                                        P.saveLoginResponse(application, user)
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


        } else {
            toastMsg_.value = "Mobile and password is empty"
        }

    }

    private fun isLoginValidate(): Boolean {
        return !U.isStrEmpty(name_.value) && !U.isStrEmpty(mobile_.value) && !U.isStrEmpty(password_.value)
    }

    fun setMobileValue(mobileStr: String) {
        mobile_.value = mobileStr
    }

    fun setPasswordValue(passStr: String) {
        password_.value = passStr
    }

    fun setNameValue(nameStr: String) {
        name_.value = nameStr
    }

}