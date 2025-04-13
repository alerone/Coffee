package com.alvarobrivaro.coffee.ui.makerecipe

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
import com.alvarobrivaro.coffee.domain.models.IngredientWithQuantity
import com.alvarobrivaro.coffee.ui.makecoffee.RecipesList
import com.alvarobrivaro.coffee.ui.theme.CoffeeTheme
import com.alvarobrivaro.coffee.ui.theme.Vainilla70
import com.alvarobrivaro.coffee.ui.theme.Vainilla90
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
    var showDialog by remember { mutableStateOf(false) }

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
        GetRecipeState.Loading -> {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is GetRecipeState.Success -> {
            val recipes = (getRecipeState as GetRecipeState.Success).recipes
            if (recipes.isEmpty()) {
                EmptyView(modifier)
            } else {
                Box(modifier = modifier.fillMaxSize()) {
                    RecipesList(modifier = Modifier.fillMaxWidth(), recipes = recipes)
                    FabAdd(Modifier.align(Alignment.BottomEnd)) { showDialog = true }
                }
            }
        }
    }

    if (showDialog) {
        AddRecipeDialog(
            show = showDialog,
            viewModel = viewModel,
            onDismiss = { showDialog = false },
            onConfirm = { showDialog = false },
            ingredientsState = getIngredientsState
        )
    }
}

@Composable
fun AddRecipeDialog(
    show: Boolean,
    viewModel: MakeRecipeViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    ingredientsState: GetIngredientState
) {
    var recipeName by remember { mutableStateOf("") }
    var recipeDescription by remember { mutableStateOf("") }
    var selectedIngredients by remember { mutableStateOf<List<IngredientWithQuantity>>(emptyList()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.making_recipe),
                    contentDescription = "Create recipe",
                    modifier = Modifier
                        .size(110.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Create New Recipe",
                    style = MaterialTheme.typography.titleLarge,
                    color = Vainilla90
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                OutlinedTextField(
                    value = recipeName,
                    onValueChange = { recipeName = it },
                    label = { Text("🧾 Name", color = Vainilla70) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = recipeDescription,
                    onValueChange = { recipeDescription = it },
                    label = { Text("👉 Description", color = Vainilla70) },
                    modifier = Modifier.fillMaxWidth()
                )

                when (ingredientsState) {
                    is GetIngredientState.Success -> {
                        val ingredients = (ingredientsState as GetIngredientState.Success).recipes
                        IngredientList(
                            ingredients = ingredients,
                            selectedIngredients = selectedIngredients,
                            onIngredientSelected = { ingredient, quantity, unit ->
                                val newIngredient = IngredientWithQuantity(
                                    ingredient = ingredient,
                                    quantity = quantity,
                                    unit = unit
                                )
                                selectedIngredients = selectedIngredients + newIngredient
                            },
                            ingredientUnits = viewModel.ingredientUnits
                        )
                    }
                    GetIngredientState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is GetIngredientState.Error -> {
                        Text("Error loading ingredients")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.createRecipe(recipeName, recipeDescription, selectedIngredients)
                    onConfirm()
                },
                enabled = recipeName.isNotBlank() && recipeDescription.isNotBlank() && selectedIngredients.isNotEmpty()
            ) {
                Text("✅ Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("❌ Cancel")
            }
        }
    )
}

@Composable
fun IngredientList(
    ingredients: List<Ingredient>,
    selectedIngredients: List<IngredientWithQuantity>,
    onIngredientSelected: (Ingredient, Double, String) -> Unit,
    ingredientUnits: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIngredient by remember { mutableStateOf<Ingredient?>(null) }
    var quantity by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("") }

    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Vainilla90
            )
        }

        selectedIngredients.forEach { ingredient ->
            Text(
                text = "${ingredient.ingredient.name}: ${ingredient.quantity} ${ingredient.unit}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedIngredient?.name ?: "🍱 Select Ingredient")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ingredients.forEach { ingredient ->
                DropdownMenuItem(
                    text = { Text(ingredient.name) },
                    onClick = {
                        selectedIngredient = ingredient
                        expanded = false
                    }
                )
            }
        }

        var unitExpanded by remember { mutableStateOf(false) }
        OutlinedButton(
            onClick = { unitExpanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedUnit.ifEmpty { "🧃 Select Unit" })
        }
        DropdownMenu(
            expanded = unitExpanded,
            onDismissRequest = { unitExpanded = false }
        ) {
            ingredientUnits.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit) },
                    onClick = {
                        selectedUnit = unit
                        unitExpanded = false
                    }
                )
            }
        }

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("📦 Quantity", color = Vainilla70) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    if (selectedIngredient != null && quantity.isNotBlank() && selectedUnit.isNotBlank()) {
                        onIngredientSelected(
                            selectedIngredient!!,
                            quantity.toDoubleOrNull() ?: 0.0,
                            selectedUnit
                        )
                        selectedIngredient = null
                        quantity = ""
                        selectedUnit = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add ingredient",
                    tint = Vainilla90
                )
            }
            Text(
                text = "👈 Add more",
                color = Vainilla90
            )
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
        FabAdd(Modifier.align(Alignment.BottomEnd)) { }
    }
}

@Composable
fun FabAdd(modifier: Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 3.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        modifier = modifier
            .padding(16.dp),
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
