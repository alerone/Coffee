package com.alvarobrivaro.coffee.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.alvarobrivaro.coffee.ui.inventory.InventoryScreen
import com.alvarobrivaro.coffee.ui.makecoffee.MakeCoffeeScreen
import com.alvarobrivaro.coffee.ui.makerecipe.MakeRecipeScreen

@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        mainViewModel.initializeDB()
    }
    MainScaffold()
}

@Preview
@Composable
fun MainScaffold() {
    var index by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            val title = when (index) {
                0 -> "Make Coffee"
                1 -> "Make Recipe"
                2 -> "Inventory"
                else -> "Make Coffee"
            }
            MyTopAppBar(title)
        },
        bottomBar = { MyBottomAppBar(index, onNavigationClick = { index = it }) }
    )
    { padding ->
        AnimatedContent(targetState = index, label = "") {
            when (it) {
                0 -> MakeCoffeeScreen(Modifier.padding(padding))
                1 -> MakeRecipeScreen(Modifier.padding(padding))
                2 -> InventoryScreen(Modifier.padding(padding))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(title: String, onNavigationClick: () -> Unit = {}) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Medium) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Composable
fun MyBottomAppBar(index: Int = 0, onNavigationClick: (Int) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Coffee, contentDescription = "Coffee page") },
            selected = index == 0,
            onClick = { onNavigationClick(0) },
            colors = NavigationBarItemDefaults.colors(
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = Color.Transparent
            ),
            label = { Text(text = "Make Coffee") }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Description,
                    contentDescription = "Recipe page"
                )
            },
            selected = index == 1,
            onClick = { onNavigationClick(1) },
            colors = NavigationBarItemDefaults.colors(
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = Color.Transparent
            ),
            label = { Text(text = "Make Recipe") }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Inventory,
                    contentDescription = "Inventory page"
                )
            },
            selected = index == 2,
            onClick = { onNavigationClick(2) },
            colors = NavigationBarItemDefaults.colors(
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = Color.Transparent
            ),
            label = { Text(text = "Inventory") }
        )

    }
}

