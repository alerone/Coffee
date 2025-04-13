package com.alvarobrivaro.coffee.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.alvarobrivaro.coffee.data.ingredient.IngredientEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeIngredientEntity
import com.alvarobrivaro.coffee.data.recipe.RecipeWithIngredients
import com.alvarobrivaro.coffee.domain.models.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface CoffeeAppDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertRecipeIngredient(recipeIngredient: RecipeIngredientEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertIngredient(ingredient: IngredientEntity): Long

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeWithIngredients(recipeId: Long): RecipeWithIngredients

    @Transaction
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeWithIngredients>>

    @Query("SELECT COUNT(*) FROM ingredients")
    suspend fun getIngredientsCount(): Int

    @Transaction
    @Query("SELECT * FROM ingredients")
    fun getAllIngredients(): Flow<List<IngredientEntity>>

    @Query("DELETE FROM recipes")
    suspend fun deleteAllRecipes()

    @Query("DELETE FROM ingredients")
    suspend fun deleteAllIngredients()

    @Query("DELETE FROM recipe_ingredients")
    suspend fun deleteAllRecipeIngredients()
}