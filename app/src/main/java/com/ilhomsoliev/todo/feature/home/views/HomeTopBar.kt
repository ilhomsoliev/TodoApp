package com.ilhomsoliev.todo.feature.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.shared.theme.AppTheme
import com.ilhomsoliev.todo.shared.theme.TodoTheme

@Composable
@Preview
private fun HomeTopBarPreview() {
    TodoTheme(false) {
        HomeTopBar(12, false, {})
    }
}

@Composable
@Preview
private fun HomeTopBarPreviewDark() {
    TodoTheme(true) {
        HomeTopBar(12, false, {})
    }
}

@Composable
fun HomeTopBar(
    completedItemsCount: Int,
    showCompleted: Boolean,
    onEyeIconClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(AppTheme.colorScheme.backPrimary),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .height(64.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(AppTheme.colorScheme.backPrimary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.my_tasks_todo_s),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colorScheme.labelPrimary
                    )
                    Text(
                        text = stringResource(
                            id = R.string.completed_todo_s,
                            completedItemsCount
                        ),
                        fontSize = 20.sp,
                        color = AppTheme.colorScheme.labelTertiary
                    )
                }
                IconButton(
                    onClick = onEyeIconClick
                ) {
                    Icon(
                        imageVector = if (showCompleted) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (showCompleted) stringResource(R.string.hide_completed_tasks)
                        else stringResource(R.string.show_completed_tasks),
                        tint = AppTheme.colorScheme.blue
                    )
                }
            }
        }
    }
}