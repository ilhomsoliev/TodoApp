package com.ilhomsoliev.todo.shared.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

@Stable
data class MAppColorScheme(
    val supportSeparator: Color,
    val supportOverlay: Color,
    val labelPrimary: Color,
    val labelSecondary: Color,
    val labelTertiary: Color,
    val labelDisable: Color,
    val red: Color,
    val green: Color,
    val blue: Color,
    val gray: Color,
    val grayLight: Color,
    val white: Color,
    val backPrimary: Color,
    val backSecondary: Color,
    val backElevated: Color,
)

@Stable
data class MAppShape(
    val container: Shape,
    val button: Shape,
    val circular: Shape
)

@Stable
data class MAppTypography(
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val captionMedium: TextStyle,
    val labelButton: TextStyle,
    val headlineH2: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val body3: TextStyle,
    val captionLarge: TextStyle,
)

val LocalMAppColorScheme = staticCompositionLocalOf {
    MAppColorScheme(
        supportSeparator = Color.Unspecified,
        supportOverlay = Color.Unspecified,
        labelPrimary = Color.Unspecified,
        labelSecondary = Color.Unspecified,
        labelTertiary = Color.Unspecified,
        labelDisable = Color.Unspecified,
        red = Color.Unspecified,
        green = Color.Unspecified,
        blue = Color.Unspecified,
        gray = Color.Unspecified,
        grayLight = Color.Unspecified,
        white = Color.Unspecified,
        backPrimary = Color.Unspecified,
        backSecondary = Color.Unspecified,
        backElevated = Color.Unspecified,
    )
}

val LocalMAppTypography = staticCompositionLocalOf {
    MAppTypography(
        titleLarge = TextStyle.Default,
        titleMedium = TextStyle.Default,
        captionMedium = TextStyle.Default,
        labelButton = TextStyle.Default,
        headlineH2 = TextStyle.Default,
        body1 = TextStyle.Default,
        body2 = TextStyle.Default,
        body3 = TextStyle.Default,
        captionLarge = TextStyle.Default,
    )
}