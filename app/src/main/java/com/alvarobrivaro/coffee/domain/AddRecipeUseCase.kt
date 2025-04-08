package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.data.CoffeeAppRepository
import com.alvarobrivaro.coffee.domain.models.Recipe
import javax.inject.Inject

class AddRecipeUseCase  @Inject constructor(private val repository: CoffeeAppRepository) {
    suspend operator fun invoke(recipe: Recipe) {
        repository.addRecipe(recipe)
    }
}