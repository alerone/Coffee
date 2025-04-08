package com.alvarobrivaro.coffee.ui.makecoffee

import com.alvarobrivaro.coffee.domain.models.Recipe

sealed class GetRecipeState {
    object Loading: GetRecipeState()
    data class Error(val throwable: Throwable) : GetRecipeState()
    data class Success(val recipes: List<Recipe>) : GetRecipeState()
}