package com.ilhomsoliev.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
@Preview
private fun TypographyPreview(modifier: Modifier = Modifier) {
    TodoTheme(false) {
        Column(modifier = Modifier.background(AppTheme.colorScheme.backPrimary)) {
            Text(text = "Large title — 32/38", style = AppTheme.typography.largeTitle)
            Text(text = "Title — 20/32", style = AppTheme.typography.title)
            Text(text = "BUTTON — 14/24", style = AppTheme.typography.button)
            Text(text = "Body — 16/20", style = AppTheme.typography.body)
            Text(text = "Subhead — 14/20", style = AppTheme.typography.subhead)
        }
    }
}

@Composable
@Preview
private fun TypographyPreviewDark(modifier: Modifier = Modifier) {
    TodoTheme(true) {
        Column(modifier = Modifier.background(AppTheme.colorScheme.backPrimary)) {
            Text(text = "Large title — 32/38", style = AppTheme.typography.largeTitle)
            Text(text = "Title — 20/32", style = AppTheme.typography.title)
            Text(text = "BUTTON — 14/24", style = AppTheme.typography.button)
            Text(text = "Body — 16/20", style = AppTheme.typography.body)
            Text(text = "Subhead — 14/20", style = AppTheme.typography.subhead)
        }
    }
}

@Composable
fun typography(): MAppTypography {
    return MAppTypography(
        largeTitle = TextStyle(
            fontSize = 32.sp,
            color = AppTheme.colorScheme.labelPrimary,
            fontWeight = FontWeight(500),
        ),
        title = TextStyle(
            fontSize = 20.sp,
            color = AppTheme.colorScheme.labelPrimary,
            fontWeight = FontWeight(500),
        ),
        button = TextStyle(
            fontSize = 14.sp,
            color = AppTheme.colorScheme.labelPrimary,
            fontWeight = FontWeight(500),
        ),
        body = TextStyle(
            fontSize = 16.sp,
            color = AppTheme.colorScheme.labelPrimary,
            fontWeight = FontWeight(500),
        ),
        subhead = TextStyle(
            fontSize = 14.sp,
            color = AppTheme.colorScheme.labelPrimary,
            fontWeight = FontWeight(400),
        ),
    )

}

val typography = MAppTypography(
    largeTitle = TextStyle(
        fontSize = 11.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight(400),
    ),
    title = TextStyle(
        fontSize = 11.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight(400),
    ),
    button = TextStyle(
        fontSize = 11.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight(400),
    ),
    body = TextStyle(
        fontSize = 11.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight(400),
    ),
    subhead = TextStyle(
        fontSize = 11.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight(400),
    ),
)
