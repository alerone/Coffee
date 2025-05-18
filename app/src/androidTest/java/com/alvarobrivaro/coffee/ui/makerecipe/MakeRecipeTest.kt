package com.alvarobrivaro.coffee.ui.makerecipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.unit.dp
import com.alvarobrivaro.coffee.domain.models.Ingredient
import com.alvarobrivaro.coffee.domain.state.GetIngredientState
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

class TestMakeRecipe {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenAddRecipeDialogGetTrue_thenShowAddRecipeDialog() {
        val getIngredientsState = GetIngredientState.Success(emptyList<Ingredient>())
        composeTestRule.setContent {
            AddRecipeDialog(
                show = true,
                onDismiss = {},
                onConfirm = { _, _, _ -> },
                ingUnits = emptyList<String>(),
                ingredientsState = getIngredientsState
            )
        }
        composeTestRule.onNodeWithTag("addRecipeDialog").assertIsDisplayed()
        sleep(1500)
    }

    @Test
    fun whenTextSentToAddRecipeDialogNameField_thenShowNameOnField() {
        val getIngredientsState = GetIngredientState.Success(emptyList<Ingredient>())
        composeTestRule.setContent {
            AddRecipeDialog(
                show = true,
                onDismiss = {},
                onConfirm = { _, _, _ -> },
                ingUnits = emptyList<String>(),
                ingredientsState = getIngredientsState
            )
        }
        sleep(1000)
        composeTestRule.onNodeWithTag("nameTextField").performTextReplacement("New Recipe test")
        sleep(1500)
        composeTestRule.onNodeWithTag("nameTextField").assertTextContains("New Recipe test")
    }

    @Test
    fun whenErrorLoadingIngredients_thenShowError() {
        composeTestRule.setContent {
            AddRecipeDialog(
                show = true,
                onDismiss = {},
                onConfirm = { _, _, _ -> },
                ingUnits = emptyList<String>(),
                ingredientsState = GetIngredientState.Error(Throwable("Error loading ingredients"))
            )
        }
        sleep(2000)
        composeTestRule.onNodeWithText("Error loading ingredients").assertIsDisplayed()
    }

    @Test
    fun whenSwipeableItemWithActionsIsSwiped_thenShowActions() {
        composeTestRule.setContent {
            Box(modifier = Modifier.fillMaxHeight().padding(16.dp), contentAlignment = Alignment.Center) {
                SwipeableItemWithActions(
                    isRevealed = false,
                    actions = {
                        ActionCard(
                            onClick = {},
                            backgroundColor = MaterialTheme.colorScheme.errorContainer,
                            icon = Icons.Default.Delete,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .fillMaxHeight(),
                        )
                    },
                    onExpanded = {},
                    onCollapsed = {},
                ) {
                    Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                        Text("This is a test")
                    }
                }
            }
        }
        sleep(1000)
        composeTestRule.onNodeWithTag("swipeableItem").performTouchInput { swipeLeft() }
        sleep(2000)
        composeTestRule.onNodeWithTag("actions").assertIsDisplayed()
    }
}

