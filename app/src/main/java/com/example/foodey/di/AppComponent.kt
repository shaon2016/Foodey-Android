package com.example.foodey.di

import android.app.Application
import com.example.foodey.MyApp
import com.example.foodey.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * This component is our key to injecting objects into our Android components (i.e.- activities, fragments etc.).
 * After building the project and let Dagger generate the implementation of AppComponent.
 * The name of the generated class will be the interface name prefixed
 * with Dagger —DaggerAppComponent in our case.
 * */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        FragmentsBindingModule::class,
        DbModule::class,
        AppModule::class,
        ActivityBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}
