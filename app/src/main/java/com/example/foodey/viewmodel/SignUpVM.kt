package com.example.foodey.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupVM : ViewModel() {

    private val name_ = MutableLiveData<String>()
    val name: LiveData<String> = name_

    private val email_ = MutableLiveData<String>()
    val email: LiveData<String> = email_

    private val password_ = MutableLiveData<String>()
    val password: LiveData<String> = password_

    private val isDataLoading_ = MutableLiveData<Boolean>()
    val isDataLoading: LiveData<Boolean> = isDataLoading_


    fun register() {
        // show progress bar
        isDataLoading_.value = true
        // save data to server

        // in response callback save data to shared pref or local db

        // hide pb

        // redirect to home page


    }


}