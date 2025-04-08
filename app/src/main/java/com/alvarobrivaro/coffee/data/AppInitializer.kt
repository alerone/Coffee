package com.alvarobrivaro.coffee.data

import com.alvarobrivaro.coffee.data.ingredient.IngredientEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeIngredientEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppInitializer @Inject constructor(
    private val dao: CoffeeAppDao
) {
    suspend fun seedIfNeeded() {
        val ingredientsCount = dao.getIngredientsCount()
        // dao.deleteAllRecipes()
        if (ingredientsCount == 0) {
            CoroutineScope(Dispatchers.IO).launch {
                val ingredients = listOf(
                    IngredientEntity(1, "Coffee"),
                    IngredientEntity(2, "Water"),
                    IngredientEntity(3, "Milk"),
                    IngredientEntity(4, "Sugar"),
                    IngredientEntity(5, "Cinnamon"),
                    IngredientEntity(7, "Milk Foam"),
                    IngredientEntity(8, "Condensed Milk"),
                    IngredientEntity(9, "Vainilla Ice Cream"),
                    IngredientEntity(10, "Chocolate"),
                )
                ingredients.forEach {
                    dao.insertIngredient(it)
                }

                val recipes = listOf(
                    RecipeEntity(
                        0,
                        "Espresso",
                        "A strong and concentrated coffee, the base of many other recipes."
                    ),
                    RecipeEntity(
                        1,
                        "Americano",
                        "An espresso diluted with hot water, ideal for a milder flavor."
                    ),
                    RecipeEntity(
                        2,
                        "Cappuccino",
                        "An Italian classic with equal parts espresso, steamed milk, and foam."
                    ),
                )

                recipes.forEach {
                    dao.insertRecipe(it)
                }

                var recipeIngredients = listOf(
                    RecipeIngredientEntity(1,0, 1, 7.0, "gr"),
                    RecipeIngredientEntity(2,0, 2, 30.0, "ml"),
                )
                recipeIngredients.forEach {
                    dao.insertRecipeIngredient(it)
                }
                recipeIngredients = listOf(
                    RecipeIngredientEntity(3,1, 1, 7.0, "gr"),
                    RecipeIngredientEntity(4,1, 2, 120.0, "ml"),
                )

                recipeIngredients.forEach {
                    dao.insertRecipeIngredient(it)
                }

                recipeIngredients = listOf(
                    RecipeIngredientEntity(5,2, 1, 7.0, "gr"),
                    RecipeIngredientEntity(6,2, 3, 60.0, "ml"),
                    RecipeIngredientEntity(6,2, 7, 60.0, "ml"),
                )
                recipeIngredients.forEach {
                    dao.insertRecipeIngredient(it)
                }
            }
        }
    }
}
