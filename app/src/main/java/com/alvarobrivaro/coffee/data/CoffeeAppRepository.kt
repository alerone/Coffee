package com.alvarobrivaro.coffee.data

import com.alvarobrivaro.coffee.data.ingredient.IngredientEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeIngredientEntity
import com.alvarobrivaro.coffee.domain.models.Ingredient
import com.alvarobrivaro.coffee.domain.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoffeeAppRepository @Inject constructor(private val coffeeAppDao: CoffeeAppDao) {
    val recipes: Flow<List<Recipe>> =
        coffeeAppDao.getAllRecipes().map { items -> items.map { it.toDomain() } }
    val ingredients: Flow<List<Ingredient>> =
        coffeeAppDao.getAllIngredients().map { items -> items.map { it.toDomain() } }

    suspend fun addRecipe(recipe: Recipe) {
        val recipeId = coffeeAppDao.insertRecipe(
            RecipeEntity(
                id = recipe.id.toLong(),
                name = recipe.name,
                description = recipe.description
            )
        )
        recipe.ingredients.forEach { ingredientWithQuantity ->
            val ingredient = ingredientWithQuantity.ingredient
            val ingredientEntity = IngredientEntity(name = ingredient.name)
            val ingredientId = coffeeAppDao.insertIngredient(ingredientEntity)

            val relation = RecipeIngredientEntity(
                recipeId = recipeId,
                ingredientId = ingredientId,
                quantity = ingredientWithQuantity.quantity,
                unit = ingredientWithQuantity.unit
            )
            coffeeAppDao.insertRecipeIngredient(relation)
        }
    }

    suspend fun deleteAllRecipes() {
        coffeeAppDao.deleteAllRecipes()
    }

}