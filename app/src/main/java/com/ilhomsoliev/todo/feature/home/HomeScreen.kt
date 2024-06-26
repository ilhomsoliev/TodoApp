package com.ilhomsoliev.todo.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ilhomsoliev.todo.feature.home.models.HomeEvent
import com.ilhomsoliev.todo.feature.home.views.HomeDisplay

@Composable
fun HomeScreen(
    vm: HomeViewModel,
    goAddTodo: (id: String?) -> Unit
) {
    val viewState by vm.viewStates().collectAsState()
    val viewAction by vm.viewActions().collectAsState(initial = null)

    HomeDisplay(state = viewState) {
        when (it) {
            is HomeEvent.ItemClick -> {
                goAddTodo(it.id)
            }

            else -> vm.obtainEvent(it)

        }
    }

}