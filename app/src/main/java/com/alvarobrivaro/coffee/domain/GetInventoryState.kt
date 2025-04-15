package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.domain.models.IngredientWithQuantity

sealed class GetInventoryState {
    object Loading : GetInventoryState()
    data class Error(val throwable: Throwable) : GetInventoryState()
    data class Success(val inventory: List<IngredientWithQuantity>) : GetInventoryState()
}