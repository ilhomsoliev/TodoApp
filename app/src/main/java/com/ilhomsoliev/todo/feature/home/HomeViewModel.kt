package com.ilhomsoliev.todo.feature.home

import com.ilhomsoliev.todo.core.BaseSharedViewModel
import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.core.on
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
                    repository.setShowCompleted(newValue)
                }
            }

            is HomeEvent.Refresh -> {
                withViewModelScope {
                    repository.updateList().on {
                        viewAction = HomeAction.ShowSnackbar("Could no update list from server")
                    }
                }
            }

            else -> {}
        }
    }

    init {
        withViewModelScope {
            repository.getTodos().on {
                viewAction = HomeAction.ShowSnackbar("Could no update list from server")
            }
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
        withViewModelScope {
            repository.getDoneTodosAmount().collect {
                viewState = viewState.copy(completedCount = it)
            }
        }
        withViewModelScope {
            repository.getShowCompleted().collect {
                viewState = viewState.copy(isShowCompletedEnabled = it)
            }
        }
    }

    private fun deleteTodoAt(todoId: String) {
        withViewModelScope {
            repository.getTodoById(todoId).on(success = { todo ->
                repository.deleteTodo(todoId).on(success = {
                    showSnackbarMessage(
                        "Удалить " + todo.text,
                        actionLabel = "Отмена",
                        onActionPerformed = {
                            withViewModelScope {
                                repository.addTodo(todo)
                            }
                        })
                }) {
                    showSnackbarMessage("Что то пошло не так!")
                }
            })
        }
    }

    private fun markTodoAsDoneAt(id: String) {
        withViewModelScope {
            val response = repository.markTodoAsValue(id)
            if (response is ResultState.Error) {
                viewAction = HomeAction.ShowSnackbar("Нету этой записи")
            }
        }
    }
}