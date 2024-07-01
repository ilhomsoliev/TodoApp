package com.ilhomsoliev.todo.shared.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilhomsoliev.todo.shared.theme.AppTheme.colorScheme
import kotlin.reflect.full.memberProperties

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
private fun PreviewColors() {
    TodoTheme {
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            MAppColorScheme::class.memberProperties.forEach { property ->
                val color = property.get(colorScheme) as Color
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(color)
                ) {
                    Text(text = property.name)
                }
            }
        }

    }
}

internal val lightColorScheme = MAppColorScheme(
    supportSeparator = Color(0xFFE0E0E0),
    supportOverlay = Color(0xFFF0F0F0),
    labelPrimary = Color(0xFF000000),
    labelSecondary = Color(0xFF808080),
    labelTertiary = Color(0xFFA0A0A0),
    labelDisable = Color(0xFFD0D0D0),
    red = Color(0xFFFF3D30),
    green = Color(0xFF30FF30),
    blue = Color(0xFF007AFF),
    gray = Color(0xFFB0B0B0),
    grayLight = Color(0xFFD0D0D0),
    white = Color(0xFFFFFFFF),
    backPrimary = Color(0xFFF0F0F0),
    backSecondary = Color(0xFFFFFFFF),
    backElevated = Color(0xFFFFFFFF),
)
internal val darkColorScheme = MAppColorScheme(
    supportSeparator = Color(0xFF3F3F3F),
    supportOverlay = Color(0xFF5D5D5D),
    labelPrimary = Color(0xFFFFFFFF),
    labelSecondary = Color(0xFFB0B0B0),
    labelTertiary = Color(0xFFD0D0D0),
    labelDisable = Color(0xFFF0F0F0),
    red = Color(0xFFFF3D30),
    green = Color(0xFF30FF30),
    blue = Color(0xFF007AFF),
    gray = Color(0xFFB0B0B0),
    grayLight = Color(0xFF404040),
    white = Color(0xFFFFFFFF),
    backPrimary = Color(0xFF181818),
    backSecondary = Color(0xFF303030),
    backElevated = Color(0xFF484848),
)