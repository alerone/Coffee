package com.alvarobrivaro.coffee.ui.makerecipe

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.alvarobrivaro.coffee.R
import com.alvarobrivaro.coffee.domain.GetIngredientState
import com.alvarobrivaro.coffee.domain.GetRecipeState
import com.alvarobrivaro.coffee.domain.models.Ingredient
import com.alvarobrivaro.coffee.ui.makecoffee.RecipesList
import com.alvarobrivaro.coffee.ui.theme.CoffeeTheme
import kotlin.collections.forEach

@Preview
@Composable
fun MakeRecipePreview() {
    CoffeeTheme(dynamicColor = false) {
        Scaffold {
            MakeRecipeScreen(modifier = Modifier.padding(it))
        }
    }
}

@Composable
fun MakeRecipeScreen(modifier: Modifier, viewModel: MakeRecipeViewModel = hiltViewModel()) {

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
    val getIngredientsState by produceState<GetIngredientState>(
        initialValue = GetIngredientState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.getIngredientsState.collect { value = it }
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
                Box(modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    RecipesList(modifier = Modifier.align(Alignment.TopCenter), recipes = recipes)
                    FabAdd(Modifier.align(Alignment.BottomEnd))
                }
            }
        }
    }
}

@Composable
fun AddRecipeDialog(show: Boolean, viewModel: MakeRecipeViewModel, onDismiss: () -> Unit, onConfirm: () -> Unit){
    var selectedIngredient by remember { mutableStateOf<Ingredient?>(null) }
    Column {
//        IngredientDropdown() { }
    }
}

@Composable
fun IngredientDropdown (ingredients : List<Ingredient>, selectedIngredient: Ingredient?, onIngredientSelected: (Ingredient) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedIngredient?.name ?: "Select an ingredient") }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        OutlinedButton(onClick = { expanded = !expanded }) {
            Row {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
                VerticalDivider(Modifier.size(2.dp))
                Text(selectedText, style = MaterialTheme.typography.bodySmall)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ingredients.forEach { ingredient ->
                DropdownMenuItem(
                    text = { Text(ingredient.name, style = MaterialTheme.typography.bodySmall) },
                    onClick = {
                        selectedText = ingredient.name
                        expanded = false
                        onIngredientSelected(ingredient)
                    }
                )
            }
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
        FabAdd(Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
fun FabAdd(modifier: Modifier) {
    FloatingActionButton(
        onClick = {},
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 3.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            modifier = Modifier.size(32.dp),
            contentDescription = "Add",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
