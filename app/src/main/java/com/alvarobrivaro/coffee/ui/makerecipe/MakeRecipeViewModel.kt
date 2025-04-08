package com.alvarobrivaro.coffee.ui.makerecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarobrivaro.coffee.domain.GetIngredientState
import com.alvarobrivaro.coffee.domain.GetIngredientState.Success
import com.alvarobrivaro.coffee.domain.GetIngredientsUseCase
import com.alvarobrivaro.coffee.domain.GetRecipesUseCase
import com.alvarobrivaro.coffee.domain.GetRecipeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MakeRecipeViewModel @Inject constructor(
    getRecipesUseCase: GetRecipesUseCase,
    getIngredientsUseCase: GetIngredientsUseCase
): ViewModel(){

    val getRecipeState: StateFlow<GetRecipeState> =
        getRecipesUseCase().map(GetRecipeState::Success).catch { GetRecipeState.Error(it) }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), GetRecipeState.Loading
        )

    val getIngredientsState: StateFlow<GetIngredientState> =
        getIngredientsUseCase().map(GetIngredientState::Success).catch { GetIngredientState.Error(it) }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), GetIngredientState.Loading
        )
}