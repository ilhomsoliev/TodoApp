package com.ilhomsoliev.todo.shared.date_picker

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ilhomsoliev.todo.shared.theme.TodoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun DatePickerDialogPreview(modifier: Modifier = Modifier) {
    TodoTheme {
        val state = rememberDatePickerState()

        DatePickerDialog(
            state = state,
            onDismissRequest = { },
            onConfirmButtonClick = { }) {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
) {
    androidx.compose.material3.DatePickerDialog(
        properties = DialogProperties(),
        colors = DatePickerDefaults.colors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmButtonClick()
                }
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissButtonClick()
                }
            ) { Text("Cancel") }
        }
    ) {
        DatePicker(
            /*colors = DatePickerDefaults.colors(containerColor = AppTheme.colorScheme.bgTertiary)
                .copy(
                    containerColor = Color.White,
                    todayDateBorderColor = AppTheme.colorScheme.textAccent,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = AppTheme.colorScheme.textAccent,
                    todayContentColor = Color.Black,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedLabelColor = AppTheme.colorScheme.textAccent,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = AppTheme.colorScheme.textAccent,
                        focusedTextColor = Color.Black,
                        unfocusedLabelColor = AppTheme.colorScheme.textAccent,
                        unfocusedContainerColor = Color.White,
                        unfocusedIndicatorColor = AppTheme.colorScheme.textAccent,
                        unfocusedTextColor = Color.Black,
                    )
                ),*/
            state = state,
            showModeToggle = true,
        )
    }
}