package com.example.foodey.di.module

import android.app.Application
import android.content.Context
import com.example.foodey.BuildConfig
import com.example.foodey.util.C
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by "Ashiq" on 01/15/2020.
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.applicationContext.cacheDir, cacheSize.toLong())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, cache: Cache) =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .readTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .cache(cache)
            .build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.BASIC
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(C.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}
