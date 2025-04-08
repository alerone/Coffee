package com.alvarobrivaro.coffee.domain.models

import android.icu.util.Calendar

data class Recipe(
    val id: Int = Calendar.getInstance().timeInMillis.hashCode(),
    val name: String,
    val description: String,
    val ingredients: List<IngredientWithQuantity>
)
