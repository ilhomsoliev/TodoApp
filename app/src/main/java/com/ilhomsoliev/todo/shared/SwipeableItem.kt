package com.ilhomsoliev.todo.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SwipeableItem(
    modifier: Modifier = Modifier,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    content: @Composable () -> Unit
) {
    val swipeThreshold = 100f
    val offsetX = remember { mutableStateOf(0f) }

    val backgroundColor = when {
        offsetX.value > 0 -> Color.Green.copy(alpha = minOf(1f, offsetX.value / swipeThreshold))
        offsetX.value < 0 -> Color.Red.copy(alpha = minOf(1f, -offsetX.value / swipeThreshold))
        else -> Color.Transparent
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetX.value > swipeThreshold) {
                            onSwipeRight()
                        } else if (offsetX.value < -swipeThreshold) {
                            onSwipeLeft()
                        }
                        offsetX.value = 0f
                    }
                ) { change, dragAmount ->
                    change.consume()
                    val newValue = offsetX.value + dragAmount
//                    if (-swipeThreshold <= newValue && newValue <= swipeThreshold)
                    offsetX.value = newValue
                }
            }
    ) {
        if (offsetX.value > 0) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Edit",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            )
        } else if (offsetX.value < 0) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            )
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .fillMaxWidth()
        ) {
            content()
        }
    }
}