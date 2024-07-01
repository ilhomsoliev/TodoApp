package com.ilhomsoliev.todo.shared.base

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SpacerH(value: Dp, modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.width(value))
}

@Composable
fun SpacerV(value: Dp, modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(value))
}

