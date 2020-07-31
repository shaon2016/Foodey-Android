package com.example.foodey.di.module

import com.example.foodey.ui.cart.CartActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun cartActivityBinding() : CartActivity
}