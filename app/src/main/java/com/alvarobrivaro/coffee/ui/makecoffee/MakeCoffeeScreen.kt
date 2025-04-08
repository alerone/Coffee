package com.alvarobrivaro.coffee.ui.makecoffee

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.alvarobrivaro.coffee.R
import com.alvarobrivaro.coffee.domain.models.Recipe

@Composable
fun MakeCoffeeScreen(modifier: Modifier, viewModel: MakeCoffeeViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val getRecipeState by produceState<GetRecipeState>(
        initialValue = GetRecipeState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.getRecipeState.collect { value = it }
        }
    }

    when (getRecipeState) {
        is GetRecipeState.Error -> TODO()
        GetRecipeState.Loading ->
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        is GetRecipeState.Success -> {
            val recipes = (getRecipeState as GetRecipeState.Success).recipes
            if (recipes.isEmpty()) {
                EmptyView(modifier)
            } else {
                RecipesList(modifier, viewModel, recipes)
            }
        }
    }
}

@Composable
fun RecipesList(modifier: Modifier, viewModel: MakeCoffeeViewModel, recipes: List<Recipe>) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recipes, key = { it.id }) {
            RecipeCard(it)
        }
    }
}

@Composable
fun EmptyView(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.makecoffe),
                contentDescription = "No recipes available",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "No recipes available",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}