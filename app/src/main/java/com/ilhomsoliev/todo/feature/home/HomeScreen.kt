package com.ilhomsoliev.todo.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ilhomsoliev.todo.feature.home.models.HomeEvent
import com.ilhomsoliev.todo.feature.home.views.HomeDisplay
import com.master.core.viewmodel.snackbar.SnackbarMessageHandler

@Composable
fun HomeScreen(
    vm: HomeViewModel,
    goAddTodo: (id: String?) -> Unit
) {
    val viewState by vm.viewStates().collectAsState()
    val viewAction by vm.viewActions().collectAsState(initial = null)
    val snackbarMessage by vm.snackbarMessage.collectAsState()

    SnackbarMessageHandler(
        snackbarMessage = snackbarMessage,
        onDismissSnackbar = { vm.dismissSnackbar() }
    )

    HomeDisplay(state = viewState) {
        when (it) {
            is HomeEvent.ItemClick -> {
                goAddTodo(it.id)
            }

            HomeEvent.AddClick -> {
                goAddTodo(null)
            }

            else -> vm.obtainEvent(it)

        }
    }

}