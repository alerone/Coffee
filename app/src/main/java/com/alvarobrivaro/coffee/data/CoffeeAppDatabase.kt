package com.alvarobrivaro.coffee.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alvarobrivaro.coffee.data.ingredient.IngredientEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeIngredientEntity

@Database(entities = [RecipeEntity::class, IngredientEntity::class, RecipeIngredientEntity::class], version = 1)
abstract class CoffeeAppDatabase: RoomDatabase() {
    abstract fun coffeeAppDao(): CoffeeAppDao
}