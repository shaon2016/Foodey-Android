package com.example.foodey.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodey.data.db.AppDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(application: Application): AppDb {
        return Room.databaseBuilder(application.applicationContext, AppDb::class.java, "appdb")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideFoodDao(db: AppDb) = db.foodDao()

    @Singleton
    @Provides
    fun provideCartItemDao(db: AppDb) = db.cartItemDao()


}