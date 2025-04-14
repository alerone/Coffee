package com.alvarobrivaro.coffee.ui.makecoffee

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.alvarobrivaro.coffee.R
import com.alvarobrivaro.coffee.domain.models.Recipe
import com.alvarobrivaro.coffee.ui.theme.Vainilla80

@Composable
fun RecipeCard(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    isPreparing: Boolean = false,
    onPrepareClick: () -> Unit = {},
    showPurchaseDialog: Boolean = false
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { showDialog = true },
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.makerecipe),
                    contentDescription = "Recipe image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = recipe.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    recipe.ingredients.forEach { ingredient ->
                        Text(
                            text = "${ingredient.ingredient.name}: ${ingredient.quantity} ${ingredient.unit}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        if (isPreparing) {
            Image(
                painter = painterResource(id = R.drawable.cooking_food),
                contentDescription = "Preparing coffee",
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
                    .zIndex(1f)
            )
        }
    }

    if (showPurchaseDialog && showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cooking_coffe),
                        contentDescription = "Cooking coffee",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Purchase Beverage",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            text = {
                Text("Do you want to purchase this ${recipe.name}?", color = Vainilla80)
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showDialog = false
                        onPrepareClick()
                    }
                ) {
                    Text("✅ Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("❌ No")
                }
            }
        )
    }
}

