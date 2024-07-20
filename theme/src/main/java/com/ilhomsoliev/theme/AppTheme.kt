package com.ilhomsoliev.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun TodoTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme

    val rippleIndication = androidx.compose.material.ripple.rememberRipple()

    CompositionLocalProvider(
        LocalMAppColorScheme provides colorScheme,
        LocalIndication provides rippleIndication,
        LocalMAppTypography provides typography(),
        content = content,
    )
}

object AppTheme {
    val colorScheme: MAppColorScheme
        @Composable get() = LocalMAppColorScheme.current

    val typography: MAppTypography
        @Composable get() = LocalMAppTypography.current

}