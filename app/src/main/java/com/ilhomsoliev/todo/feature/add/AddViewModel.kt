package com.ilhomsoliev.todo.feature.add

import com.ilhomsoliev.todo.core.BaseSharedViewModel
import com.ilhomsoliev.todo.core.ErrorTypes
import com.ilhomsoliev.todo.core.ResultState
import com.ilhomsoliev.todo.core.generateRandomString
import com.ilhomsoliev.todo.core.getDeviceSerialNumber
import com.ilhomsoliev.todo.core.on
import com.ilhomsoliev.todo.domain.models.TodoModel
import com.ilhomsoliev.todo.domain.repository.TodoRepository
import com.ilhomsoliev.todo.feature.add.model.AddAction
import com.ilhomsoliev.todo.feature.add.model.AddEvent
import com.ilhomsoliev.todo.feature.add.model.AddViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: TodoRepository
) : BaseSharedViewModel<AddViewState, AddAction, AddEvent>(AddViewState()) {

    private var addJob: Job? = null
    override fun obtainEvent(viewEvent: AddEvent) {
        when (viewEvent) {
            is AddEvent.Add -> {
                addJob?.cancel()
                addJob = withViewModelScope {
                    if(!isActive) return@withViewModelScope
                    val response = repository.addTodo(
                        TodoModel(
                            id = viewState.id ?: generateRandomString(),
                            text = viewState.text,
                            priority = viewState.priority,
                            deadline = viewState.deadline ?: System.currentTimeMillis(), // TODO
                            done = false,
                            createdAt = System.currentTimeMillis(),
                            changedAt = 0,
                            color = "",
                            lastUpdatedBy = getDeviceSerialNumber()
                        )
                    )
                    if (response is ResultState.Success) {
                        viewAction = AddAction.NavigateBack
                    } else if (response is ResultState.Error) {
                        showSnackbarMessage("Что то пошло не так!")
                        if (response.types.contains(ErrorTypes.Network))
                            viewAction = AddAction.NavigateBack
                    }
                }
            }

            is AddEvent.DeadlineChange -> {
                viewState = viewState.copy(deadline = viewEvent.date)
            }

            is AddEvent.PriorityChange -> {
                viewState = viewState.copy(priority = viewEvent.priority)
            }

            is AddEvent.TextChange -> {
                viewState = viewState.copy(text = viewEvent.text)
            }

            is AddEvent.EnterScreen -> {
                withViewModelScope {
                    viewEvent.id?.let {
                        if (it != "null")
                            repository.getTodoById(it).let { todo ->
                                if (todo is ResultState.Success) {
                                    viewState = viewState.copy(
                                        id = todo.data.id,
                                        text = todo.data.text,
                                        priority = todo.data.priority,
                                        deadline = todo.data.deadline,
                                        date = todo.data.deadline.let { it1 ->
                                            getDateForDeadline(
                                                it1
                                            )
                                        },
                                    )
                                }
                            }
                    }
                }
            }

            AddEvent.Delete -> {
                withViewModelScope {
                    viewState.id?.let {
                        repository.deleteTodo(it).on {
                            showSnackbarMessage("Что то пошло не так!")
                        }
                    }
                    viewAction = AddAction.NavigateBack
                }
            }

            is AddEvent.DateDialogIsActiveChange -> {
                viewState = viewState.copy(dateDialogActive = viewEvent.value)
            }

            is AddEvent.OnDateChange -> {
                viewState = viewState.copy(
                    date = getDateForDeadline(viewEvent.selectedDateMillis),
                    deadline = viewEvent.selectedDateMillis,
                )
            }

            is AddEvent.OnSwitchChange -> {
                if (viewEvent.value) {
                    obtainEvent(AddEvent.DateDialogIsActiveChange(true))
                } else {
                    viewState = viewState.copy(deadline = null)
                }
            }

            else -> {}
        }
    }

    private fun getDateForDeadline(timeInMillis: Long): String {
        val selectedDate = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
        }
        val dateFormatter = SimpleDateFormat("dd.MM.yyyy")
        return dateFormatter.format(selectedDate.time)
    }

}