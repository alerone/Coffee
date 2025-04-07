package com.alvarobrivaro.coffee.data.makeCoffee

data class Recipe(
    val name: String,
    val description: String,
    val ingredients: Ingredients
)

data class Ingredients(
    val coffee: Int,
    val water: Int,
    val milk: Int,
    val milkFoam: Int,
    val chocolate: Int,
    val sugar: Int,
    val condensedMilk: Int,
    val vanillaIceCream: Int,
    val whippedCream: Int
) 