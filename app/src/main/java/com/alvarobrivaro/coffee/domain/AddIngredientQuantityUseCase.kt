package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.data.CoffeeAppRepository
import javax.inject.Inject

class AddIngredientQuantityUseCase @Inject constructor(private val repository: CoffeeAppRepository) {
    suspend operator fun invoke(ingredientId: Long, quantity: Double, unit: String) {
        val existingInventory = repository.getInventoryByIngredientId(ingredientId)
        if (existingInventory != null) {
            // Actualizar cantidad existente
            val updatedQuantity = existingInventory.quantity + quantity
            val updatedInventory = existingInventory.copy(quantity = updatedQuantity)
            repository.updateInventory(updatedInventory)
        } else {
            // Insertar nuevo registro en el inventario
            repository.insertInventory(ingredientId, quantity, unit)
        }
    }
}