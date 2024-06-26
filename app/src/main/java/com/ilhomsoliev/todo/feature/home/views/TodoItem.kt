package com.ilhomsoliev.todo.feature.home.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.core.formatDate
import com.ilhomsoliev.todo.data.models.TodoItemModel
import com.ilhomsoliev.todo.data.models.TodoPriority
import com.ilhomsoliev.todo.shared.theme.AppTheme
import com.ilhomsoliev.todo.shared.theme.TodoTheme

@Composable
@Preview
fun TodoItemPreview(modifier: Modifier = Modifier) {
    TodoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colorScheme.backPrimary)
        ) {
            TodoItemModel.demos.fastForEachIndexed { index, todoItemModel ->
                TodoItem(
                    item = todoItemModel,
                    isFirst = index == 0,
                    onCheckedChange = {},
                    onClick = {})
            }
        }
    }
}

@Composable
fun TodoItem(
    item: TodoItemModel,
    isFirst: Boolean,
    onCheckedChange: ((Boolean) -> Unit), onClick: () -> Unit
) {
    val borderColorAnimated by animateColorAsState(
        targetValue = if (item.isCompleted) AppTheme.colorScheme.green else AppTheme.colorScheme.labelTertiary,
        animationSpec = tween(durationMillis = 200), label = ""
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                if (isFirst) RoundedCornerShape(
                    topEnd = 12.dp,
                    topStart = 12.dp
                ) else RoundedCornerShape(0.dp)
            )
            .background(AppTheme.colorScheme.backSecondary)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            colors = CheckboxDefaults.colors().copy(
                checkedBoxColor = borderColorAnimated,
                checkedBorderColor = borderColorAnimated,
                uncheckedBorderColor = if (item.priority == TodoPriority.HIGH) AppTheme.colorScheme.red
                else CheckboxDefaults.colors().uncheckedBorderColor,
                uncheckedBoxColor = if (item.priority == TodoPriority.HIGH) AppTheme.colorScheme.red.copy(
                    alpha = 0.3f
                )
                else CheckboxDefaults.colors().uncheckedBoxColor,
            ),
            modifier = Modifier,
            checked = item.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Row(modifier = Modifier.weight(1f)) {
            if (item.priority != TodoPriority.USUAL) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = if (item.priority == TodoPriority.LOW) R.drawable.low_priority else R.drawable.high_priority),
                    contentDescription = stringResource(
                        R.string.low_priority_icon
                    )
                )
            }
            Column {
                Text(text = item.text, color = AppTheme.colorScheme.labelPrimary)
                item.deadline?.let {
                    Text(
                        text = formatDate(item.deadline),
                        color = AppTheme.colorScheme.labelTertiary
                    )
                }
            }
        }
        Icon(
            modifier = Modifier.padding(start = 4.dp, end = 12.dp),
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.warning_icon),
            tint = AppTheme.colorScheme.labelTertiary
        )
    }
}