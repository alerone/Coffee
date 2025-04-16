package com.alvarobrivaro.coffee.ui.inventory

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.alvarobrivaro.coffee.domain.models.Ingredient
import com.alvarobrivaro.coffee.domain.models.IngredientWithQuantity
import com.alvarobrivaro.coffee.domain.state.GetInventoryState
import com.alvarobrivaro.coffee.ui.theme.CoffeeTheme

@Composable
fun InventoryScreen(modifier: Modifier, viewModel: InventoryViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val getInventoryState by produceState<GetInventoryState>(
        initialValue = GetInventoryState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.getInventoryState.collect { value = it }
        }
    }

    when (getInventoryState) {
        is GetInventoryState.Error -> TODO()
        GetInventoryState.Loading ->
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        is GetInventoryState.Success -> {
            val inventory = (getInventoryState as GetInventoryState.Success).inventory
            if (inventory.isEmpty()) {
                EmptyInventoryView(modifier)
            } else {
                InventoryList(
                    modifier,
                    inventory,
                    onAddClick = { viewModel.onAddIngredient(it) },
                    onRemoveClick = { viewModel.onRemoveIngredient(it) })
            }
        }
    }
}

@Composable
fun InventoryList(
    modifier: Modifier,
    inventory: List<IngredientWithQuantity>,
    onAddClick: (IngredientWithQuantity) -> Unit = {},
    onRemoveClick: (IngredientWithQuantity) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(inventory, key = { it.ingredient.id }) { item ->
            InventoryCard(item, { onAddClick(item) }, { onRemoveClick(item) })
        }
    }
}

@Preview()
@Composable
fun PreviewInventoryCard() {
    CoffeeTheme(dynamicColor = false, darkTheme = true) {
        InventoryCard(
            item = IngredientWithQuantity(
                ingredient = Ingredient(
                    id = 0,
                    name = "Condensed Milk"
                ),
                quantity = 200.0,
                unit = "ml"
            )
        )
    }
}

@Composable
fun InventoryCard(
    item: IngredientWithQuantity,
    onAddClick: (IngredientWithQuantity) -> Unit = {},
    onRemoveClick: (IngredientWithQuantity) -> Unit = {}
) {
    val imageResource = remember(item.ingredient.name) {
        getImageResourceByIngredient(item.ingredient.name)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Ingredient Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.ingredient.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${item.quantity} ${item.unit}",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(Modifier.weight(1f)) {
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = { onRemoveClick(item) }) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Remove")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { onAddClick(item) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }
}


/**
 * Returns a random image resource based on the ingredient name.
 * Specific cases for "water", ingredients containing "milk" and
 * "coffee" are handled. Other cases are just random images.
 * @param name The name of the ingredient.
 * @return The corresponding image resource.
 *
 */
fun getImageResourceByIngredient(name: String): Int {
    return when {
        name.equals("water", ignoreCase = true) -> R.drawable.water
        name.contains("milk", ignoreCase = true) -> listOf(
            R.drawable.big_cup,
            R.drawable.coffee_cream,
            R.drawable.milk
        ).random()
        name.equals("coffee", ignoreCase = true) -> R.drawable.coffee
        else -> listOf(
            R.drawable.ingredient,
            R.drawable.ingredient_pack,
            R.drawable.inventory,
            R.drawable.capsule
        ).random()
    }
}

@Composable
fun EmptyInventoryView(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ingredient),
                contentDescription = "No inventory available",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "No inventory available",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}