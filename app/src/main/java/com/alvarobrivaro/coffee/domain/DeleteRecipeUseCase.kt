package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.data.CoffeeAppRepository
import javax.inject.Inject

class DeleteRecipeUseCase @Inject constructor(private val repository: CoffeeAppRepository) {
    suspend operator fun invoke(recipeId: Long) {
        repository.deleteRecipe(recipeId)
    }
}
