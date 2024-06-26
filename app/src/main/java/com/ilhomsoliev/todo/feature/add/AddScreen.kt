package com.ilhomsoliev.todo.feature.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ilhomsoliev.todo.feature.add.model.AddEvent
import com.ilhomsoliev.todo.feature.add.views.AddDisplay

@Composable
fun AddScreen(vm: AddViewModel, onBack: () -> Unit) {
    val viewState by vm.viewStates().collectAsState()
    val viewAction by vm.viewActions().collectAsState(initial = null)

    AddDisplay(state = viewState) {
        when (it) {
            is AddEvent.OnBack -> onBack()
            else -> vm.obtainEvent(it)
        }
    }
}