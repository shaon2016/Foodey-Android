package com.example.foodey.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

fun <T : ViewModel> AppCompatActivity.obtainViewModel(
    viewModelClass: Class<T>,
    viewmodelFactory: ViewModelProvider.Factory
) =
    ViewModelProvider(this, viewmodelFactory).get(viewModelClass)

