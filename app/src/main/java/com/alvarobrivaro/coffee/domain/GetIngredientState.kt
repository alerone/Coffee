package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.domain.models.Ingredient
import com.alvarobrivaro.coffee.domain.models.Recipe

sealed class GetIngredientState {
    object Loading: GetIngredientState()
    data class Error(val throwable: Throwable) : GetIngredientState()
    data class Success(val recipes: List<Ingredient>) : GetIngredientState()
}