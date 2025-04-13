package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.data.CoffeeAppRepository
import javax.inject.Inject

class GetIngredientUnitsUseCase @Inject constructor(private val repository: CoffeeAppRepository) {
    operator fun invoke(): List<String> {
        return listOf("gr", "ml")
    }
}