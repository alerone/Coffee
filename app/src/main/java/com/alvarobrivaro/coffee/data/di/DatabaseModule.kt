package com.alvarobrivaro.coffee.data.di

import android.content.Context
import androidx.room.Room
import com.alvarobrivaro.coffee.data.CoffeeAppDao
import com.alvarobrivaro.coffee.data.CoffeeAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideCofeeAppDao(db: CoffeeAppDatabase): CoffeeAppDao =
        db.coffeeAppDao()

    @Provides
    @Singleton
    fun provideCoffeeAppDatabase(@ApplicationContext context: Context) : CoffeeAppDatabase {
        return Room.databaseBuilder(context, CoffeeAppDatabase::class.java, "CoffeeAppDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }
}