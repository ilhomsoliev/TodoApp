package com.ilhomsoliev.todo.feature.home.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ilhomsoliev.todo.data.models.TodoItemModel

@Composable
fun TodoItem(item: TodoItemModel, onCheckedChange: ((Boolean) -> Unit), onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Checkbox(checked = item.isCompleted, onCheckedChange = onCheckedChange)
        Text(text = item.text)
        Icon(imageVector = Icons.Default.Info, contentDescription = null)
    }
}