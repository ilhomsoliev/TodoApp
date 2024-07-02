package com.ilhomsoliev.todo.feature.home

import com.ilhomsoliev.todo.core.BaseSharedViewModel
import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.domain.repository.TodoRepository
import com.ilhomsoliev.todo.feature.home.models.HomeAction
import com.ilhomsoliev.todo.feature.home.models.HomeEvent
import com.ilhomsoliev.todo.feature.home.models.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TodoRepository
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
                withViewModelScope {
                    val newValue = !viewState.isShowCompletedEnabled
                    viewState = viewState.copy(isShowCompletedEnabled = newValue)
//                    repository.setShowCompleted(newValue)
                }
            }

            else -> {}
        }
    }

    init {
        withViewModelScope {
            repository.getTodos()
        }
        withViewModelScope {
            repository.observeTodos().collect {
                if (it is ResultState.Success) {
                    viewState = viewState.copy(todos = it.data)
                } else {
                    viewAction = HomeAction.ShowSnackbar("Some error while loading")
                }
            }
        }
        /*withViewModelScope {
            repository.getDoneTodosAmount().collect {
                if (it is ResultState.Success) {
                    viewState = viewState.copy(completedCount = it.data)
                } else {
                    viewAction = HomeAction.ShowSnackbar("Some error while loading amount")
                }
            }
        }*/
        /*withViewModelScope {
            repository.getShowCompleted().collect {
                viewState = viewState.copy(isShowCompletedEnabled = it)
            }
        }*/
    }

    private fun deleteTodoAt(todoId: String) {
        withViewModelScope {
            repository.deleteTodo(todoId)
        }
    }

    private fun markTodoAsDoneAt(id: String) {
        withViewModelScope {
            // TODO
            /*val response = repository.markTodoAsValue(id)
            if (response is ResultState.Error) {
                viewAction = HomeAction.ShowSnackbar("Нету этой записи")
            }*/
        }
    }
}