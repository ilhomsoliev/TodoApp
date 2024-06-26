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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.ilhomsoliev.todo.data.models.TodoPriority
import com.ilhomsoliev.todo.feature.add.model.AddEvent
import com.ilhomsoliev.todo.feature.add.model.AddViewState
import com.ilhomsoliev.todo.shared.base.SpacerV
import com.ilhomsoliev.todo.shared.date_picker.DatePickerDialog
import com.ilhomsoliev.todo.shared.textfield.FeedbackTextField
import com.ilhomsoliev.todo.shared.theme.AppTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDisplay(
    state: AddViewState,
    callback: (AddEvent) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val dateState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val DAY_IN_MILLIS = 86_400_000
            val currentTimeMillis = System.currentTimeMillis().minus(DAY_IN_MILLIS)
            return utcTimeMillis > currentTimeMillis
        }

        override fun isSelectableYear(year: Int): Boolean {
            val currentYear = LocalDate.now().year
            return year >= currentYear
        }
    })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppTheme.colorScheme.backPrimary,
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = AppTheme.colorScheme.backPrimary),
                navigationIcon = {
                    IconButton(onClick = { callback(AddEvent.OnBack) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = AppTheme.colorScheme.labelPrimary
                        )
                    }
                }, actions = {
                    Text(
                        modifier = Modifier.clickable {
                            callback(AddEvent.Add)
                        },
                        text = "Сохранить",
                        color = Color(0xFF007AFF),
                    )
                }
            )
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
                        expanded = !expanded
                    }) {
                    Text(
                        text = "Важность",
                        color = AppTheme.colorScheme.labelPrimary,
                        fontSize = 16.sp
                    )
                    Text(
                        text = state.priority.nameRu,
                        color = AppTheme.colorScheme.labelTertiary,
                        fontSize = 16.sp,
                    )
                    SpacerV(value = 12.dp)

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        TodoPriority.entries.fastForEach {
                            DropdownMenuItem(
                                text = { Text(it.nameRu) },
                                onClick = {
                                    callback(AddEvent.PriorityChange(it))
                                    expanded = false
                                }
                            )
                        }
                    }

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
                                text = "Сделать до",
                                color = AppTheme.colorScheme.labelPrimary,
                                fontSize = 16.sp
                            )
                            state.deadline?.let {
                                Text(
                                    text = state.date.toString(),
                                    color = AppTheme.colorScheme.blue,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Switch(checked = state.deadline != null, onCheckedChange = {
                            callback(AddEvent.DateDialogIsActiveChange(true))
                        })
                    }

                    SpacerV(value = 12.dp)

                    HorizontalDivider()
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