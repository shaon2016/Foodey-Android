package com.example.foodey.di.module

import com.example.foodey.ui.cart.CartActivity
import com.example.foodey.ui.checkout.CheckoutActivity
import com.example.foodey.ui.login.LoginActivity
import com.example.foodey.ui.signup.SignUpActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun cartActivityBinding() : CartActivity

    @ContributesAndroidInjector
    abstract fun checkoutActivityBinding() : CheckoutActivity

    @ContributesAndroidInjector
    abstract fun loginActivityBinding() : LoginActivity

    @ContributesAndroidInjector
    abstract fun signUpActivityBinding() : SignUpActivity
}