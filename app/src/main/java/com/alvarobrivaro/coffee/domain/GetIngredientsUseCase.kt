package com.alvarobrivaro.coffee.domain

import com.alvarobrivaro.coffee.data.CoffeeAppRepository
import javax.inject.Inject

class GetIngredientsUseCase @Inject constructor(private val repository: CoffeeAppRepository){
    operator fun invoke() = repository.ingredients
}

