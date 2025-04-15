package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.data.CoffeeAppRepository
import javax.inject.Inject

class AddIngredientUseCase @Inject constructor(private val repository: CoffeeAppRepository) {
    suspend operator fun invoke(name: String): Long {
        return repository.insertIngredient(name)
    }
}