package com.example.foodey

import com.example.foodey.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MyApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return  DaggerAppComponent.builder().application(this).build()
    }
}