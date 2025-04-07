package com.alvarobrivaro.coffee.ui.makecoffee

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarobrivaro.coffee.R
import com.alvarobrivaro.coffee.data.makeCoffee.Recipe
import com.alvarobrivaro.coffee.data.makeCoffee.RecipeRepository

@Composable
fun MakeCoffeeScreen(modifier: Modifier) {
    val context = LocalContext.current
    val recipeRepository = remember { RecipeRepository(context) }
    var recipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }
    var preparingRecipes by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(Unit) {
        recipes = recipeRepository.getRecipes()
    }

    if (recipes.isEmpty()) {
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
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    isPreparing = preparingRecipes.contains(recipe.name),
                    onPrepareClick = { 
                        preparingRecipes = if (preparingRecipes.contains(recipe.name)) {
                            preparingRecipes - recipe.name
                        } else {
                            preparingRecipes + recipe.name
                        }
                    }
                )
            }
        }
    }
}