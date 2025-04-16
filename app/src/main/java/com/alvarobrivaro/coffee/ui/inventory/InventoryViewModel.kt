package com.alvarobrivaro.coffee.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarobrivaro.coffee.domain.AddIngredientQuantityUseCase
import com.alvarobrivaro.coffee.domain.GetInventoryUseCase
import com.alvarobrivaro.coffee.domain.models.IngredientWithQuantity
import com.alvarobrivaro.coffee.domain.state.GetInventoryState
import com.alvarobrivaro.coffee.domain.state.GetInventoryState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val addIngredientQuantityUseCase: AddIngredientQuantityUseCase,
    getInventoryUseCase: GetInventoryUseCase
) : ViewModel() {

    val getInventoryState: StateFlow<GetInventoryState> =
        getInventoryUseCase().map(::Success).catch { GetInventoryState.Error(it) }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), GetInventoryState.Loading
        )

    fun onAddIngredient(ingredient: IngredientWithQuantity, increment: Double = 10.0) {
        viewModelScope.launch {
            val newQuantity = ingredient.copy(quantity = ingredient.quantity + increment)
            addIngredientQuantityUseCase(newQuantity)
        }
    }

    fun onRemoveIngredient(ingredient: IngredientWithQuantity, increment: Double = 10.0) {
        viewModelScope.launch {
            val newQuantity = ingredient.copy(quantity = ingredient.quantity - increment)
            addIngredientQuantityUseCase(newQuantity)
        }
    }

}