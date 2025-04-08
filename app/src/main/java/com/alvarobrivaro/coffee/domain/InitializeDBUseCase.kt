package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.data.AppInitializer
import com.alvarobrivaro.coffee.data.CoffeeAppRepository
import javax.inject.Inject

class InitializeDBUseCase @Inject constructor(private val initializer: AppInitializer) {
    suspend operator fun invoke() {
        initializer.seedIfNeeded()
    }
}