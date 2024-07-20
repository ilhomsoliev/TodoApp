package com.ilhomsoliev.todo.feature.add.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.theme.AppTheme
import com.ilhomsoliev.theme.TodoTheme

@Composable
@Preview
private fun AddTopBarPreview() {
    TodoTheme(false) {
        AddTopBar({}, {})
    }
}

@Composable
@Preview
private fun AddTopBarPreviewDark() {
    TodoTheme(true) {
        AddTopBar({}, {})
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopBar(onBack: () -> Unit, onAdd: () -> Unit) {
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors()
            .copy(containerColor = AppTheme.colorScheme.backPrimary),
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = AppTheme.colorScheme.labelPrimary
                )
            }
        }, actions = {
            Text(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable {
                        onAdd()
                    },
                text = stringResource(R.string.save),
                color = Color(0xFF007AFF),
            )
        }
    )
}