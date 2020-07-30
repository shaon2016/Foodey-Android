package com.example.foodey

import android.app.Application
import com.example.foodey.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector

class MyApp : Application(), HasAndroidInjector {

    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        // this can be called after the AppComponent setup and rebuild the project
        DaggerAppComponent.create().inject(this)
    }
}