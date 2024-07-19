package com.ilhomsoliev.todo.feature.add.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.data.models.TodoPriority
import com.ilhomsoliev.todo.feature.add.model.AddEvent
import com.ilhomsoliev.todo.feature.add.model.AddViewState
import com.ilhomsoliev.todo.shared.base.SpacerH
import com.ilhomsoliev.todo.shared.base.SpacerV
import com.ilhomsoliev.todo.shared.date_picker.DatePickerDialog
import com.ilhomsoliev.todo.shared.textfield.FeedbackTextField
import com.ilhomsoliev.todo.shared.theme.AppTheme
import com.ilhomsoliev.todo.shared.theme.TodoTheme
import java.util.Calendar

@Composable
@Preview
fun AddDisplayPreview() {
    TodoTheme {
        AddDisplay(state = AddViewState(), {})
    }
}

@Composable
@Preview
fun AddDisplayPreviewDark() {
    TodoTheme(isDarkTheme = true) {
        AddDisplay(state = AddViewState(), {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDisplay(
    state: AddViewState,
    callback: (AddEvent) -> Unit,
) {

    val dateState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val MILLIS_IN_DAY = 86_400_000
            val currentTimeMillis = System.currentTimeMillis().minus(MILLIS_IN_DAY)
            return utcTimeMillis > currentTimeMillis
        }

        override fun isSelectableYear(year: Int): Boolean {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            return year >= currentYear
        }
    })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppTheme.colorScheme.backPrimary,
        topBar = {
            AddTopBar(onBack = {
                callback(AddEvent.OnBack)
            }, onAdd = {
                callback(AddEvent.Add)
            })
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            item {
                FeedbackTextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                    value = state.text,
                    onValue = {
                        callback(AddEvent.TextChange(it))
                    })
                SpacerV(value = 12.dp)
            }
            item {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clickable {
                        callback(AddEvent.PriorityChange(TodoPriority.LOW))
                    }) {
                    Text(
                        text = stringResource(R.string.priority),
                        color = AppTheme.colorScheme.labelPrimary,
                        fontSize = 16.sp
                    )
                    Text(
                        text = state.priority.nameRu,
                        color = AppTheme.colorScheme.labelTertiary,
                        fontSize = 16.sp,
                    )
                    SpacerV(value = 12.dp)
                    HorizontalDivider()
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { callback(AddEvent.DateDialogIsActiveChange(true)) }
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.todo_until),
                                color = AppTheme.colorScheme.labelPrimary,
                                fontSize = 16.sp
                            )
                            state.deadline?.let {
                                Text(
                                    text = state.date,
                                    color = AppTheme.colorScheme.blue,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Switch(checked = state.deadline != null, onCheckedChange = {
                            callback(AddEvent.OnSwitchChange(it))
                        })
                    }

                    SpacerV(value = 12.dp)

                    HorizontalDivider()
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { callback(AddEvent.Delete) }
                        .padding(vertical = 12.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.trash_icon),
                        tint = AppTheme.colorScheme.red
                    )
                    SpacerH(value = 6.dp)

                    Text(text = stringResource(R.string.delete), color = AppTheme.colorScheme.red)

                }
            }
        }
    }
    if (state.dateDialogActive) {
        DatePickerDialog(
            state = dateState,
            onDismissRequest = {
                callback(AddEvent.DateDialogIsActiveChange(false))
            },
            onConfirmButtonClick = {
                callback(AddEvent.DateDialogIsActiveChange(false))
                dateState.selectedDateMillis?.let {
                    callback(AddEvent.OnDateChange(it))
                }
            },
            onDismissButtonClick = {
                callback(AddEvent.DateDialogIsActiveChange(false))
            }
        )
    }
}