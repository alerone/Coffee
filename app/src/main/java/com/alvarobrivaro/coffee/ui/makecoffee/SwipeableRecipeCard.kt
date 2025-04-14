package com.alvarobrivaro.coffee.ui.makecoffee

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.alvarobrivaro.coffee.domain.models.Recipe
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.swipeable
import androidx.wear.compose.material.rememberSwipeableState

enum class SwipeCardState { DEFAULT, CONFIRM }

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeableRecipeCard(
    recipe: Recipe,
    onDelete: (Long) -> Unit
) {
    val swipeState = rememberSwipeableState(initialValue = SwipeCardState.DEFAULT)

    // Definimos el threshold
    val thresholdDp = 250.dp
    val density = LocalDensity.current
    val thresholdPx = with(density) { thresholdDp.toPx() }

    val offsetX by animateFloatAsState(targetValue = swipeState.offset.value)

    LaunchedEffect(swipeState.currentValue) {
        if (swipeState.currentValue == SwipeCardState.CONFIRM) {
            delay(3000L)
            if (swipeState.currentValue == SwipeCardState.CONFIRM) {
                swipeState.animateTo(SwipeCardState.DEFAULT)
            }
        }
    }

    val showRed = swipeState.currentValue == SwipeCardState.CONFIRM ||
            swipeState.offset.value <= -thresholdPx

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeState,
                anchors = mapOf(
                    0f to SwipeCardState.DEFAULT,
                    -thresholdPx to SwipeCardState.CONFIRM
                ),
                thresholds = { _, _ -> FractionalThreshold(0.7f) },
                orientation = Orientation.Horizontal
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(if (showRed) Color.Red else Color.Transparent)
                .padding(end = 30.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (showRed) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Recipe",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp,40.dp)
                        .clickable {
                        onDelete(recipe.id.toLong())
                    }
                )
            }
        }
        Box(
            modifier = Modifier.offset { IntOffset(offsetX.roundToInt(), 0) }
        ) {
            RecipeCard(recipe, showPurchaseDialog = false)
        }
    }
}
