package com.example.foodey.di.module

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodey.di.ViewModelKey
import com.example.foodey.ui.home.HomeVM
import com.example.foodey.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
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
  @ViewModelKey(HomeVM::class)
  abstract fun bindHomeViewModel(homeVM: HomeVM): ViewModel

  @Binds
  abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
