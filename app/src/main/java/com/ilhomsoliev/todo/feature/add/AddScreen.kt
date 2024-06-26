package com.ilhomsoliev.todo.feature.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ilhomsoliev.todo.feature.add.model.AddAction
import com.ilhomsoliev.todo.feature.add.model.AddEvent
import com.ilhomsoliev.todo.feature.add.views.AddDisplay

@Composable
fun AddScreen(vm: AddViewModel, onBack: () -> Unit, id: String?) {

    val viewState by vm.viewStates().collectAsState()
    val viewAction by vm.viewActions().collectAsState(initial = null)

    LaunchedEffect(key1 = Unit) {
        vm.obtainEvent(AddEvent.EnterScreen(id))
    }
    LaunchedEffect(key1 = viewAction) {
        when (viewAction) {
            AddAction.NavigateBack -> onBack()
//            is AddAction.ShowSnackbar -> TODO()
            else -> {}
        }
    }

    AddDisplay(state = viewState) {
        when (it) {
            is AddEvent.OnBack -> onBack()
            else -> vm.obtainEvent(it)
        }
    }
}