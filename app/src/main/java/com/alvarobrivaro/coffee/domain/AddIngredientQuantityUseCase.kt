package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.data.CoffeeAppRepository
import com.alvarobrivaro.coffee.domain.models.IngredientWithQuantity
import javax.inject.Inject

class AddIngredientQuantityUseCase @Inject constructor(private val repository: CoffeeAppRepository) {
    suspend operator fun invoke(ingredientWithQ: IngredientWithQuantity) {
        val existingInventory = repository.getInventoryByIngredientId(ingredientWithQ.ingredient.id)
        if (existingInventory != null) {
            val updatedInventory = existingInventory.copy(quantity = ingredientWithQ.quantity)
            repository.updateInventory(updatedInventory)
        } else {
            repository.insertInventory(ingredientWithQ.ingredient.id, ingredientWithQ.quantity, ingredientWithQ.unit)
        }
    }
}