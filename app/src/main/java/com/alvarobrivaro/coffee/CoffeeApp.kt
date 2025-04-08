package com.alvarobrivaro.coffee

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CoffeeApp @Inject constructor() : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}