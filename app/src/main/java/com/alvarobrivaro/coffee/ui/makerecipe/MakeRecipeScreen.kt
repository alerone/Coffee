package com.alvarobrivaro.coffee.ui.makerecipe

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

    AddRecipeDialog(
        show = showDialog,
        viewModel = viewModel,
        onDismiss = { showDialog = false },
        onConfirm = { showDialog = false },
        ingredientsState = getIngredientsState
    )
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
    if (!show) return
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.making_recipe),
                    contentDescription = "Create recipe",
                    modifier = Modifier
                        .size(90.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Create New Recipe",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                CoffeeTextField(
                    modifier = Modifier,
                    value = recipeName,
                    onValueChange = { recipeName = it },
                    label = "🧾 Name"
                )

                CoffeeTextField(
                    modifier = Modifier,
                    value = recipeDescription,
                    onValueChange = { recipeDescription = it },
                    label = "👉 Description"
                )
                when (ingredientsState) {
                    is GetIngredientState.Success -> {
                        val ingredients = ingredientsState.recipes
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                enabled = recipeName.isNotBlank() && recipeDescription.isNotBlank() && selectedIngredients.isNotEmpty()
            ) {
                Text("✅ Create")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("❌ Cancel")
            }
        }
    )
}

@Composable
fun PreviewTextField() {
    CoffeeTheme(dynamicColor = false, darkTheme = false) {
        Scaffold(containerColor = MaterialTheme.colorScheme.background) {
            CoffeeTextField(Modifier.padding(it), "", {}, "test")
        }
    }
}

@Composable
fun CoffeeTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        )
    )
}
@Composable
fun MatchaTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.tertiaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.tertiaryContainer,
            focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    )
}


@Composable
fun IngredientList(
    ingredients: List<Ingredient>,
    selectedIngredients: List<IngredientWithQuantity>,
    onIngredientSelected: (Ingredient, Double, String) -> Unit,
    ingredientUnits: List<String>
) {
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
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (selectedIngredients.isNotEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                selectedIngredients.forEach { ingredient ->
                    Text(
                        text = "${ingredient.ingredient.name}: ${ingredient.quantity} ${ingredient.unit}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        DropdownMenuIngredients(
            ingredients,
            selectedIngredient = selectedIngredient,
            onSubmit = { ingredient -> selectedIngredient = ingredient })
        Spacer(Modifier.height(8.dp))

        DropdownMenuUnits(
            ingredientUnits,
            selectedUnit = selectedUnit,
            onSubmit = { unit -> selectedUnit = unit })

        Spacer(Modifier.height(8.dp))

        MatchaTextField(
            modifier = Modifier.fillMaxWidth(),
            value = quantity,
            onValueChange = { quantity = it },
            label = "📦 Quantity",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
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
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "👈 Add",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuIngredients(
    ingredients: List<Ingredient> = emptyList(),
    onSubmit: (Ingredient) -> Unit = {},
    selectedIngredient: Ingredient? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },

            ) {
            OutlinedTextField(
                value = selectedIngredient?.name ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                placeholder = { Text("🍱 Ingredients") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                ingredients.forEach { ingredient ->
                    DropdownMenuItem(
                        text = { Text(ingredient.name) },
                        onClick = {
                            expanded = false
                            onSubmit(ingredient)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuUnits(
    units: List<String> = emptyList(),
    onSubmit: (String) -> Unit = {},
    selectedUnit: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },

            ) {
            OutlinedTextField(
                value = selectedUnit,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                placeholder = { Text("🧃 Select Unit") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            expanded = false
                            onSubmit(unit)
                        }
                    )
                }
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
