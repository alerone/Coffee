package com.alvarobrivaro.coffee.domain.state

import com.alvarobrivaro.coffee.domain.models.Ingredient

sealed class GetIngredientState {
    object Loading: GetIngredientState()
    data class Error(val throwable: Throwable) : GetIngredientState()
    data class Success(val recipes: List<Ingredient>) : GetIngredientState()
}