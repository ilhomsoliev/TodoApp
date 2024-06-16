package com.ilhomsoliev.todo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ilhomsoliev.todo.core.BaseSharedViewModel
import com.ilhomsoliev.todo.data.repository.TodoItemsRepository
import com.ilhomsoliev.todo.feature.home.models.HomeAction
import com.ilhomsoliev.todo.feature.home.models.HomeEvent
import com.ilhomsoliev.todo.feature.home.models.HomeViewState
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TodoItemsRepository
) : BaseSharedViewModel<HomeViewState, HomeAction, HomeEvent>(HomeViewState()) {

    override fun obtainEvent(viewEvent: HomeEvent) {
        when (viewEvent) {
            is HomeEvent.MarkItem -> {
                markTodoAsDoneAt(viewEvent.id)
            }

            is HomeEvent.DeleteItem -> {
                deleteTodoAt(viewEvent.id)
            }
        }
    }

    init {
        viewModelScope.launch {
            repository.getTodos().collect {
                viewState = viewState.copy(todos = it)
            }
        }
    }

    private fun deleteTodoAt(todoId: String) {
        repository.deleteTodo(todoId)
    }

    private fun markTodoAsDoneAt(id: String) {
        val response = repository.markTodoAsValue(id)
        if (!response) {
            // TODO
        }
    }
}

class HomeViewModelFactory(private val repository: TodoItemsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}