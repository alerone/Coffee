package com.alvarobrivaro.coffee.ui.makecoffee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarobrivaro.coffee.domain.GetRecipeState
import com.alvarobrivaro.coffee.domain.GetRecipesUseCase
import com.alvarobrivaro.coffee.domain.GetRecipeState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MakeCoffeeViewModel @Inject constructor(getRecipesUseCase: GetRecipesUseCase) :
    ViewModel() {
    val getRecipeState: StateFlow<GetRecipeState> =
        getRecipesUseCase().map(::Success).catch { GetRecipeState.Error(it) }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), GetRecipeState.Loading
        )
}