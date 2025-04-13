package com.alvarobrivaro.coffee.data

import android.content.Context
import com.alvarobrivaro.coffee.data.ingredient.IngredientEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeIngredientEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.alvarobrivaro.coffee.data.models.JsonRecipe

class AppInitializer @Inject constructor(
    private val dao: CoffeeAppDao,
    private val context: Context
) {
    suspend fun seedIfNeeded() {
        //dao.deleteAllRecipeIngredients()
        //dao.deleteAllIngredients()
        //dao.deleteAllRecipes()
        val ingredientsCount = dao.getIngredientsCount()

        if (ingredientsCount == 0) {
            CoroutineScope(Dispatchers.IO).launch {
                val jsonString = context.assets.open("listOfRecipes.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<JsonRecipe>>() {}.type
                val recipes = Gson().fromJson<List<JsonRecipe>>(jsonString, type)

            val uniqueIngredients = recipes.flatMap { it.ingredients }
                .distinctBy { it.id }
                .map { IngredientEntity(it.id.toLong(), it.name) }

            uniqueIngredients.forEach { dao.insertIngredient(it) }

            recipes.forEach { jsonRecipe ->
                val recipe = RecipeEntity(
                    jsonRecipe.id.toLong(),
                    jsonRecipe.name,
                    jsonRecipe.description
                )
                dao.insertRecipe(recipe)

                    jsonRecipe.ingredients.forEach { jsonIngredient ->
                        val recipeIngredient = RecipeIngredientEntity(
                            id = 0, // Auto-generated
                            recipeId = jsonRecipe.id.toLong(),
                            ingredientId = jsonIngredient.id.toLong(),
                            quantity = jsonIngredient.quantity,
                            unit = jsonIngredient.unit
                        )
                        dao.insertRecipeIngredient(recipeIngredient)
                    }
                }
            }
        }
    }
}
