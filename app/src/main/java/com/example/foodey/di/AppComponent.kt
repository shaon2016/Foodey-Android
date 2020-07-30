package com.example.foodey.di

import com.example.foodey.MyApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * This component is our key to injecting objects into our Android components (i.e.- activities, fragments etc.).
 * After building the project and let Dagger generate the implementation of AppComponent.
 * The name of the generated class will be the interface name prefixed
 * with Dagger —DaggerAppComponent in our case.
 * */
@Component(
    modules = [
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApp> {
}