package com.example.foodey.di.module

import com.example.foodey.ui.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsBindingModule {

    @ContributesAndroidInjector
    abstract fun bindingHomeFragment() : HomeFragment


}