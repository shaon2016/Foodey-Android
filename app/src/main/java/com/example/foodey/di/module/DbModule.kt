package com.example.foodey.di.module

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
    fun provideRoomDatabase(context: Context): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "dbName")
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