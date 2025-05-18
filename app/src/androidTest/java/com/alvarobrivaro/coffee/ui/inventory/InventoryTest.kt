package com.alvarobrivaro.coffee.ui.inventory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.alvarobrivaro.coffee.domain.models.Ingredient
import com.alvarobrivaro.coffee.domain.models.IngredientWithQuantity
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

class InventoryTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenInventoryItemShown_thenNameOfItemIsShownOnCard() {
        val testIngredient = IngredientWithQuantity(
            ingredient = Ingredient(
                id = 1,
                name = "TestIngredient"
            ),
            quantity = 10.0,
            unit = "gr"
        )
        composeTestRule.setContent {
            InventoryCard(testIngredient)
        }
        sleep(1500)
        composeTestRule.onNodeWithTag("inventoryItemName").assertTextContains("TestIngredient")
    }

    @Test
    fun whenAddOrRemoveButtonAreClicked_thenQuantityIsUpdated() {
        composeTestRule.setContent {
            var testQuantity by remember { mutableStateOf(100.0) }
            val testIngredient = IngredientWithQuantity(
                ingredient = Ingredient(
                    id = 1,
                    name = "TestIngredient"
                ),
                quantity = testQuantity,
                unit = "gr"
            )
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                InventoryCard(testIngredient, onIncrease = { testQuantity += 10.0 }, onDecrease = { testQuantity -= 10.0})
            }
        }

        sleep(3000)
        composeTestRule.onNodeWithTag("ingredientQuantity")
            .assertTextContains("100.0 gr")
        composeTestRule.onNodeWithTag("decreaseIngredientButton").performClick()
        sleep(1000)
        composeTestRule.onNodeWithTag("decreaseIngredientButton").performClick()
        sleep(1000)
        composeTestRule.onNodeWithTag("ingredientQuantity")
            .assertTextContains("80.0 gr")
        composeTestRule.onNodeWithTag("addIngredientButton").performClick()
        sleep(1000)
        composeTestRule.onNodeWithTag("addIngredientButton").performClick()
        sleep(1000)
        composeTestRule.onNodeWithTag("ingredientQuantity")
            .assertTextContains("100.0 gr")

    }
}
