package com.alvarobrivaro.coffee.data.models

data class JsonRecipe(
        val id: Int,
        val name: String,
        val description: String,
        val ingredients: List<JsonIngredient>
)

data class JsonIngredient(
    val id: Int,
    val name: String,
    val quantity: Double,
    val unit: String
)