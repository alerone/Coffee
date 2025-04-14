package com.alvarobrivaro.coffee.ui.makerecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarobrivaro.coffee.domain.AddRecipeUseCase
import com.alvarobrivaro.coffee.domain.DeleteRecipeUseCase
import com.alvarobrivaro.coffee.domain.GetIngredientState
import com.alvarobrivaro.coffee.domain.GetIngredientState.Success
import com.alvarobrivaro.coffee.domain.GetIngredientUnitsUseCase
import com.alvarobrivaro.coffee.domain.GetIngredientsUseCase
import com.alvarobrivaro.coffee.domain.GetRecipesUseCase
import com.alvarobrivaro.coffee.domain.GetRecipeState
import com.alvarobrivaro.coffee.domain.models.Ingredient
import com.alvarobrivaro.coffee.domain.models.IngredientWithQuantity
import com.alvarobrivaro.coffee.domain.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeRecipeViewModel @Inject constructor(
    getRecipesUseCase: GetRecipesUseCase,
    getIngredientsUseCase: GetIngredientsUseCase,
    private val addRecipeUseCase: AddRecipeUseCase,
    private val getIngredientUnitsUseCase: GetIngredientUnitsUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
): ViewModel(){

    val getRecipeState: StateFlow<GetRecipeState> =
        getRecipesUseCase().map(GetRecipeState::Success).catch { GetRecipeState.Error(it) }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), GetRecipeState.Loading
        )

    val getIngredientsState: StateFlow<GetIngredientState> =
        getIngredientsUseCase().map { ingredients -> 
            GetIngredientState.Success(ingredients)
        }.catch { GetIngredientState.Error(it) }.stateIn(
            viewModelScope, 
            SharingStarted.WhileSubscribed(5000), 
            GetIngredientState.Loading
        )

    fun createRecipe(name: String, description: String, ingredients: List<IngredientWithQuantity>) {
        viewModelScope.launch {
            val recipe = Recipe(
                name = name,
                description = description,
                ingredients = ingredients
            )
            addRecipeUseCase(recipe)
        }
    }

    val ingredientUnits: List<String> = getIngredientUnitsUseCase()

    fun deleteRecipe(recipeId: Long) {
        viewModelScope.launch {
            deleteRecipeUseCase(recipeId)
        }
    }

}