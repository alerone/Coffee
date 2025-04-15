package com.alvarobrivaro.coffee.ui.makerecipe.model

import com.alvarobrivaro.coffee.domain.models.Recipe

data class RecipeUI(
    val recipe: Recipe,
    var isOptionsVisible: Boolean = false
)