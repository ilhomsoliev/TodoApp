package com.ilhomsoliev.todo.feature.add

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.ilhomsoliev.todo.data.models.TodoPriority
import com.ilhomsoliev.todo.feature.add.model.AddAction
import com.ilhomsoliev.todo.feature.add.model.AddEvent
import com.ilhomsoliev.todo.feature.add.views.AddDisplay
import com.ilhomsoliev.todo.shared.base.SpacerV
import com.ilhomsoliev.todo.shared.snackbar.SnackbarMessageHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(vm: AddViewModel, onBack: () -> Unit, id: String?) {

    val viewState by vm.viewStates().collectAsState()
    val viewAction by vm.viewActions().collectAsState(initial = null)
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetActive by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        vm.obtainEvent(AddEvent.EnterScreen(id))
    }

    LaunchedEffect(key1 = viewAction) {
        when (viewAction) {
            AddAction.NavigateBack -> onBack()
            else -> {}
        }
    }
    val snackbarMessage by vm.snackbarMessage.collectAsState()


    SnackbarMessageHandler(
        snackbarMessage = snackbarMessage,
        onDismissSnackbar = { vm.dismissSnackbar() }
    )
    if (isBottomSheetActive)
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    isBottomSheetActive = false
                    sheetState.hide()
                }
            },
            modifier = Modifier,
            sheetState = sheetState
        ) {
            Column {
                TodoPriority.entries.fastForEach {
                    DropdownMenuItem(
                        text = { Text(it.nameRu) },
                        onClick = {
                            vm.obtainEvent(AddEvent.PriorityChange(it))
                            scope.launch {
                                sheetState.hide()
                                isBottomSheetActive = false
                            }

                        }
                    )
                }
                SpacerV(value = 12.dp)

            }
        }

    AddDisplay(state = viewState) {
        when (it) {
            is AddEvent.OnBack -> onBack()
            is AddEvent.PriorityChange -> {
                scope.launch {
                    sheetState.expand()
                    isBottomSheetActive = true
                }
            }

            else -> vm.obtainEvent(it)
        }
    }


}