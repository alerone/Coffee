package com.alvarobrivaro.coffee.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarobrivaro.coffee.domain.GetInventoryState
import com.alvarobrivaro.coffee.domain.GetInventoryUseCase
import com.alvarobrivaro.coffee.domain.GetInventoryState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(getInventoryUseCase: GetInventoryUseCase) :
    ViewModel() {
    val getInventoryState: StateFlow<GetInventoryState> =
        getInventoryUseCase().map(::Success).catch { GetInventoryState.Error(it) }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), GetInventoryState.Loading
        )
}