package com.ilhomsoliev.todo.feature.home

import com.ilhomsoliev.todo.core.BaseSharedViewModel
import com.ilhomsoliev.todo.data.repository.TodoItemsRepository
import com.ilhomsoliev.todo.feature.home.models.HomeAction
import com.ilhomsoliev.todo.feature.home.models.HomeEvent
import com.ilhomsoliev.todo.feature.home.models.HomeViewState

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

            HomeEvent.ToggleIsCompletedVisible -> {
                val newValue = !viewState.isShowCompletedEnabled
                viewState = viewState.copy(isShowCompletedEnabled = newValue)
                repository.setShowCompleted(newValue)
            }

            else -> {}
        }
    }

    init {
        withViewModelScope {
            repository.getTodos().collect {
                viewState = viewState.copy(todos = it)
            }
        }
        withViewModelScope {
            repository.getDoneTodosAmount().collect {
                viewState = viewState.copy(completedCount = it)
            }
        }
    }

    private fun deleteTodoAt(todoId: String) {
        repository.deleteTodo(todoId)
    }

    private fun markTodoAsDoneAt(id: String) {
        val response = repository.markTodoAsValue(id)
        if (!response) {
            viewAction = HomeAction.ShowSnackbar("Нету этой записи")
        }
    }
}