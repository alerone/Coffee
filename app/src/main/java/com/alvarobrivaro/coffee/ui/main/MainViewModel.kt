package com.alvarobrivaro.coffee.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarobrivaro.coffee.domain.InitializeDBUseCase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val initializeDBUseCase: InitializeDBUseCase
) : ViewModel(){
    fun initializeDB() {
        viewModelScope.launch(Dispatchers.IO) {
            initializeDBUseCase()
        }
    }
}