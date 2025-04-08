package com.alvarobrivaro.coffee.domain.models

data class IngredientWithQuantity(
    val ingredient: Ingredient,
    val quantity: Double,
    val unit: String
)