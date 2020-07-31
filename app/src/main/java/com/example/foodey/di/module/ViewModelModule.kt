package com.example.foodey.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodey.di.annotation.ViewModelKey
import com.example.foodey.ui.cart.CartVM
import com.example.foodey.ui.checkout.CheckoutVM
import com.example.foodey.ui.home.HomeVM
import com.example.foodey.ui.login.LoginVM
import com.example.foodey.ui.order.OrderVM
import com.example.foodey.ui.signup.SignUpVM
import com.example.foodey.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 *
 *  Collected from google sample // Github Browser sample
 *
 *  */

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginVM::class)
    abstract fun bindLoginViewModel(loginVM: LoginVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpVM::class)
    abstract fun bindSignUpViewModel(signUpVM: SignUpVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeVM::class)
    abstract fun bindHomeViewModel(homeVM: HomeVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CartVM::class)
    abstract fun bindCartViewModel(cartVM: CartVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckoutVM::class)
    abstract fun bindCheckoutViewModel(checkoutVM: CheckoutVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderVM::class)
    abstract fun bindOrderViewModel(orderVM: OrderVM): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
